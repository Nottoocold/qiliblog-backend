package com.zqqiliyc.service.article;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.svc.main.dict.ArticleStatus;
import com.zqqiliyc.module.svc.main.domain.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.module.svc.main.domain.dto.article.ArticleUpdateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import com.zqqiliyc.module.svc.main.domain.entity.Category;
import com.zqqiliyc.module.svc.main.domain.entity.Tag;
import com.zqqiliyc.module.svc.main.mapper.ArticleMapper;
import com.zqqiliyc.module.svc.main.scheduler.ArticleScheduler;
import com.zqqiliyc.module.svc.main.service.IArticleService;
import com.zqqiliyc.module.svc.main.service.ICategoryService;
import com.zqqiliyc.module.svc.main.service.ITagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章写路径接口级测试：穿过 IArticleService 断言可观察行为，不等待真实 cron
 */
public class ArticleServiceTest extends BaseBlogService {
    @Autowired
    IArticleService articleService;
    @Autowired
    ICategoryService categoryService;
    @Autowired
    ITagService tagService;
    @Autowired
    ArticleScheduler articleScheduler;

    @Test
    @Order(0)
    public void testCreateDraftException() {
        // 发布时间不能早于当前时间
        ArticleDraftCreateDTO dto = baseDto();
        dto.setPublishAt(LocalDateTime.now().minusHours(3));
        Assertions.assertThrowsExactly(ClientException.class, () -> articleService.createDraft(dto));
    }

    @Test
    @Order(1)
    public void testPublishArticle() {
        ArticleDraftCreateDTO dto = baseDto();
        dto.setPublishAt(LocalDateTime.now().plusMinutes(1));
        Article draft = articleService.createDraft(dto);
        Assertions.assertEquals(ArticleStatus.DRAFT.intVal(), draft.getStatus());

        articleService.publishArticle(draft.getId());
        Article published = articleService.findById(draft.getId());
        Assertions.assertEquals(ArticleStatus.PUBLISHED.intVal(), published.getStatus());
        Assertions.assertNotNull(published.getPublishedTime());
        Assertions.assertNull(published.getPublishAt());

        // 幂等：重复发布不改变发布时间
        LocalDateTime publishedTime = published.getPublishedTime();
        articleService.publishArticle(draft.getId());
        Assertions.assertEquals(publishedTime, articleService.findById(draft.getId()).getPublishedTime());

        articleService.deleteById(draft.getId());
    }

    @Test
    @Order(2)
    public void testScheduledPublish() {
        ArticleDraftCreateDTO dto = baseDto();
        dto.setPublishAt(LocalDateTime.now().plusMinutes(1));
        Article draft = articleService.createDraft(dto);

        // 未到发布时间：不在待发布列表
        Assertions.assertTrue(articleService.findPendingPublishArticles().stream()
                .noneMatch(a -> a.getId().equals(draft.getId())));

        // 测试夹具：把发布时间拨到过去，模拟到点
        ArticleMapper mapper = sqlSessionTemplate.getMapper(ArticleMapper.class);
        mapper.wrapper().set(Article::getPublishAt, LocalDateTime.now().minusMinutes(1))
                .eq(Article::getId, draft.getId()).update();
        Assertions.assertTrue(articleService.findPendingPublishArticles().stream()
                .anyMatch(a -> a.getId().equals(draft.getId())));

        // 直接触发调度任务，不等待真实 cron
        articleScheduler.publishScheduledArticles();

        Article published = articleService.findById(draft.getId());
        Assertions.assertEquals(ArticleStatus.PUBLISHED.intVal(), published.getStatus());
        Assertions.assertNotNull(published.getPublishedTime());

        articleService.deleteById(draft.getId());
    }

    @Test
    @Order(3)
    public void testCountConservation() {
        List<Category> categories = getCategoryList();
        List<Tag> tags = getTagList();
        Category catA = categories.get(0);
        Category catB = categories.get(1);
        Tag t1 = tags.get(0);
        Tag t2 = tags.get(1);
        Tag t3 = tags.get(2);

        int catA0 = categoryService.findById(catA.getId()).getPostCount();
        int catB0 = categoryService.findById(catB.getId()).getPostCount();
        int t10 = tagService.findById(t1.getId()).getPostCount();
        int t20 = tagService.findById(t2.getId()).getPostCount();
        int t30 = tagService.findById(t3.getId()).getPostCount();

        // 创建：catA +1，t1/t2 +1，t3 不变
        ArticleDraftCreateDTO dto = baseDto();
        dto.setCategoryId(catA.getId());
        dto.setTagIds(List.of(t1.getId(), t2.getId()));
        Article article = articleService.createDraft(dto);
        Assertions.assertEquals(catA0 + 1, categoryService.findById(catA.getId()).getPostCount());
        Assertions.assertEquals(t10 + 1, tagService.findById(t1.getId()).getPostCount());
        Assertions.assertEquals(t20 + 1, tagService.findById(t2.getId()).getPostCount());
        Assertions.assertEquals(t30, tagService.findById(t3.getId()).getPostCount());

        // 更新：换分类 catA→catB，换标签 [t1,t2]→[t2,t3]
        ArticleUpdateDTO u = new ArticleUpdateDTO();
        u.setId(article.getId());
        u.setCategoryId(catB.getId());
        u.setTagIds(List.of(t2.getId(), t3.getId()));
        articleService.updateArticle(u);
        Assertions.assertEquals(catA0, categoryService.findById(catA.getId()).getPostCount());
        Assertions.assertEquals(catB0 + 1, categoryService.findById(catB.getId()).getPostCount());
        Assertions.assertEquals(t10, tagService.findById(t1.getId()).getPostCount());
        Assertions.assertEquals(t20 + 1, tagService.findById(t2.getId()).getPostCount());
        Assertions.assertEquals(t30 + 1, tagService.findById(t3.getId()).getPostCount());

        // 删除：全部回到基线
        articleService.deleteById(article.getId());
        Assertions.assertEquals(catA0, categoryService.findById(catA.getId()).getPostCount());
        Assertions.assertEquals(catB0, categoryService.findById(catB.getId()).getPostCount());
        Assertions.assertEquals(t10, tagService.findById(t1.getId()).getPostCount());
        Assertions.assertEquals(t20, tagService.findById(t2.getId()).getPostCount());
        Assertions.assertEquals(t30, tagService.findById(t3.getId()).getPostCount());
    }

    @Test
    @Order(4)
    public void testDraftWithoutPublishAtNeverPending() {
        // publishAt 是定时发布的唯一事实：不设置就永不进入待发布列表
        Article draft = articleService.createDraft(baseDto());
        Assertions.assertNull(draft.getPublishAt());
        Assertions.assertTrue(articleService.findPendingPublishArticles().stream()
                .noneMatch(a -> a.getId().equals(draft.getId())));
        articleService.deleteById(draft.getId());
    }

    @Test
    @Order(5)
    public void testSlugValidation() {
        Article a1 = articleService.createDraft(baseDto());
        Article a2 = articleService.createDraft(baseDto());

        // 创建时指定重复 slug → 友好 400（此前 DuplicateKeyException 穿透为 500）
        ArticleDraftCreateDTO dupCreate = baseDto();
        dupCreate.setSlug(a1.getSlug());
        Assertions.assertThrowsExactly(ClientException.class, () -> articleService.createDraft(dupCreate));

        // 更新为已存在的 slug → 拒绝
        ArticleUpdateDTO dup = new ArticleUpdateDTO();
        dup.setId(a2.getId());
        dup.setSlug(a1.getSlug());
        Assertions.assertThrowsExactly(ClientException.class, () -> articleService.updateArticle(dup));

        // 更新为非法格式 → 拒绝
        ArticleUpdateDTO bad = new ArticleUpdateDTO();
        bad.setId(a2.getId());
        bad.setSlug("非法 slug!");
        Assertions.assertThrowsExactly(ClientException.class, () -> articleService.updateArticle(bad));

        // 自身 slug 不变可正常更新
        ArticleUpdateDTO ok = new ArticleUpdateDTO();
        ok.setId(a2.getId());
        ok.setSlug(a2.getSlug());
        Assertions.assertDoesNotThrow(() -> articleService.updateArticle(ok));

        articleService.deleteById(a1.getId());
        articleService.deleteById(a2.getId());
    }

    @Test
    @Order(6)
    public void testUpdateClearsPublishAt() {
        ArticleDraftCreateDTO dto = baseDto();
        dto.setPublishAt(LocalDateTime.now().plusMinutes(1));
        Article draft = articleService.createDraft(dto);
        Assertions.assertNotNull(draft.getPublishAt());

        // 全量更新语义：publishAt=null 取消定时发布
        ArticleUpdateDTO u = new ArticleUpdateDTO();
        u.setId(draft.getId());
        u.setPublishAt(null);
        articleService.updateArticle(u);
        Assertions.assertNull(articleService.findById(draft.getId()).getPublishAt());
        Assertions.assertTrue(articleService.findPendingPublishArticles().stream()
                .noneMatch(a -> a.getId().equals(draft.getId())));

        articleService.deleteById(draft.getId());
    }

    private ArticleDraftCreateDTO baseDto() {
        ArticleDraftCreateDTO dto = new ArticleDraftCreateDTO();
        dto.setTitle("测试文章-" + RandomUtil.randomString(6));
        dto.setContent(generateString(3, () -> "测试文章内容"));
        List<Category> categories = getCategoryList();
        dto.setCategoryId(categories.get(RandomUtil.randomInt(categories.size())).getId());
        List<Tag> tags = getTagList();
        dto.setTagIds(tags.stream().map(Tag::getId).toList());
        dto.setAuthorId(1L);
        return dto;
    }
}
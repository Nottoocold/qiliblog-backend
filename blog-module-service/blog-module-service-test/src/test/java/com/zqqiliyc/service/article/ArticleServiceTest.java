package com.zqqiliyc.service.article;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.svc.main.dict.ArticleStatus;
import com.zqqiliyc.module.svc.main.domain.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import com.zqqiliyc.module.svc.main.domain.entity.Category;
import com.zqqiliyc.module.svc.main.domain.entity.Tag;
import com.zqqiliyc.module.svc.main.service.IArticleService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2026-01-11
 */
public class ArticleServiceTest extends BaseBlogService {
    @Autowired
    IArticleService articleService;

    @Test
    @Order(0)
    public void testCreateDraftException() {
        ArticleDraftCreateDTO draftSaveDTO = getArticleDraftCreateDTO();
        draftSaveDTO.setDelayPublish(true);

        Assertions.assertThrowsExactly(ClientException.class, () -> articleService.createDraft(draftSaveDTO));

        draftSaveDTO.setPublishAt(LocalDateTime.now().minusHours(3));

        Assertions.assertThrowsExactly(ClientException.class, () -> articleService.createDraft(draftSaveDTO));
    }

    @Test
    @Order(1)
    public void testScheduleArticle() {
        ArticleDraftCreateDTO draftSaveDTO = getArticleDraftCreateDTO();
        draftSaveDTO.setDelayPublish(true);
        draftSaveDTO.setPublishAt(LocalDateTime.now().plusMinutes(1));
        Article draft = articleService.createDraft(draftSaveDTO);
        Assertions.assertNotNull(draft);
        Assertions.assertEquals(ArticleStatus.DRAFT.intVal(), draft.getStatus());

        try {
            TimeUnit.MINUTES.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Article article = articleService.findById(draft.getId());
        Assertions.assertEquals(ArticleStatus.PUBLISHED.intVal(), article.getStatus());
        Assertions.assertNotNull(article.getPublishedTime());
    }

    private @NonNull ArticleDraftCreateDTO getArticleDraftCreateDTO() {
        ArticleDraftCreateDTO draftSaveDTO = new ArticleDraftCreateDTO();
        draftSaveDTO.setTitle("测试文章1");
        draftSaveDTO.setContent(generateString(10, () -> "测试文章内容"));
        List<Category> categoryList = getCategoryList();
        draftSaveDTO.setCategoryId(categoryList.get(RandomUtil.randomInt(categoryList.size())).getId());
        List<Tag> tagList = getTagList();
        draftSaveDTO.setTagIds(tagList.stream().map(Tag::getId).toList());
        draftSaveDTO.setAuthorId(1L);
        return draftSaveDTO;
    }
}

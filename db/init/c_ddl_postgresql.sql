-- 删除并创建角色表
DROP TABLE IF EXISTS sys_role CASCADE;
CREATE TABLE sys_role
(
    id          BIGINT PRIMARY KEY,
    code        VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    sort        INT                   DEFAULT 0,
    state       INT          NOT NULL DEFAULT 0,
    remark      TEXT,
    create_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '主键ID';
COMMENT ON COLUMN sys_role.code IS '角色编码';
COMMENT ON COLUMN sys_role.name IS '角色名称';
COMMENT ON COLUMN sys_role.sort IS '排序';
COMMENT ON COLUMN sys_role.state IS '状态 0-启用 1-禁用';
COMMENT ON COLUMN sys_role.remark IS '备注';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';

CREATE UNIQUE INDEX idx_code ON sys_role (code);

-- 删除并创建权限表
DROP TABLE IF EXISTS sys_permission CASCADE;
CREATE TABLE sys_permission
(
    id          BIGINT PRIMARY KEY,
    code        VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    parent_id   BIGINT       NOT NULL,
    sort        INT       DEFAULT 0,
    state       INT       DEFAULT 0,
    remark      TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_permission IS '权限表';
COMMENT ON COLUMN sys_permission.id IS '主键ID';
COMMENT ON COLUMN sys_permission.code IS '权限标识';
COMMENT ON COLUMN sys_permission.name IS '权限名称';
COMMENT ON COLUMN sys_permission.parent_id IS '父级权限ID, 0表示顶级权限';
COMMENT ON COLUMN sys_permission.sort IS '排序';
COMMENT ON COLUMN sys_permission.state IS '状态 0:正常 1:禁用';
COMMENT ON COLUMN sys_permission.remark IS '备注';
COMMENT ON COLUMN sys_permission.create_time IS '创建时间';
COMMENT ON COLUMN sys_permission.update_time IS '更新时间';

CREATE UNIQUE INDEX idx_permission_code ON sys_permission (code);

-- 删除并创建系统用户表
DROP TABLE IF EXISTS sys_user CASCADE;
CREATE TABLE sys_user
(
    id          BIGINT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    nickname    VARCHAR(255),
    state       INT       DEFAULT 0,
    email       VARCHAR(255),
    phone       VARCHAR(20),
    avatar      VARCHAR(255),
    dept_id     BIGINT    DEFAULT -1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_flag    INT       DEFAULT 0
);

COMMENT ON TABLE sys_user IS '系统用户表';
COMMENT ON COLUMN sys_user.id IS '主键ID';
COMMENT ON COLUMN sys_user.username IS '用户名 登录名';
COMMENT ON COLUMN sys_user.password IS '密码 加密';
COMMENT ON COLUMN sys_user.nickname IS '昵称';
COMMENT ON COLUMN sys_user.state IS '状态 0:正常 1:禁用';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.avatar IS '头像URL';
COMMENT ON COLUMN sys_user.dept_id IS '部门ID';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.del_flag IS '删除标志 0:未删除 1:已删除';

CREATE UNIQUE INDEX idx_username ON sys_user (del_flag, username);
CREATE UNIQUE INDEX idx_email ON sys_user (del_flag, email);
CREATE UNIQUE INDEX idx_phone ON sys_user (del_flag, phone);

-- 删除并创建用户角色关联表
DROP TABLE IF EXISTS sys_user_role CASCADE;
CREATE TABLE sys_user_role
(
    id          BIGINT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_user_role IS '用户角色关联表';
COMMENT ON COLUMN sys_user_role.id IS '主键ID';
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';
COMMENT ON COLUMN sys_user_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role.update_time IS '更新时间';

CREATE UNIQUE INDEX idx_user_role ON sys_user_role (user_id, role_id);

-- 删除并创建角色权限关联表
DROP TABLE IF EXISTS sys_role_permission CASCADE;
CREATE TABLE sys_role_permission
(
    id            BIGINT PRIMARY KEY,
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_role_permission IS '角色权限关联表';
COMMENT ON COLUMN sys_role_permission.id IS '主键ID';
COMMENT ON COLUMN sys_role_permission.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_permission.permission_id IS '权限ID';
COMMENT ON COLUMN sys_role_permission.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_permission.update_time IS '更新时间';

CREATE UNIQUE INDEX idx_role_permission ON sys_role_permission (role_id, permission_id);

-- 删除并创建分类表
DROP TABLE IF EXISTS blog_category CASCADE;
CREATE TABLE blog_category
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(64) NOT NULL,
    slug        VARCHAR(64) NOT NULL,
    post_count  INT         NOT NULL DEFAULT 0,
    description VARCHAR(255)         DEFAULT NULL,
    create_time TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE blog_category IS '文章分类表';
COMMENT ON COLUMN blog_category.id IS '主键';
COMMENT ON COLUMN blog_category.name IS '分类名称 (必须唯一，如 "技术", "生活")';
COMMENT ON COLUMN blog_category.slug IS '分类 URL 友好标识符 (必须唯一，如 "tech", "life")';
COMMENT ON COLUMN blog_category.post_count IS '文章数, 用于统计分类下文章数';
COMMENT ON COLUMN blog_category.description IS '分类描述';
COMMENT ON COLUMN blog_category.create_time IS '创建时间';
COMMENT ON COLUMN blog_category.update_time IS '最后更新时间';

CREATE UNIQUE INDEX idx_category_name ON blog_category (name);
CREATE UNIQUE INDEX idx_category_slug ON blog_category (slug);

-- 删除并创建标签表
DROP TABLE IF EXISTS blog_tags CASCADE;
CREATE TABLE blog_tags
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(64) NOT NULL,
    slug        VARCHAR(64) NOT NULL,
    post_count  INT         NOT NULL DEFAULT 0,
    description VARCHAR(255)         DEFAULT NULL,
    create_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE blog_tags IS '文章标签表';
COMMENT ON COLUMN blog_tags.id IS '标签唯一标识符 (主键)';
COMMENT ON COLUMN blog_tags.name IS '标签名称 (必须唯一，如 "Java", "Spring Boot")';
COMMENT ON COLUMN blog_tags.slug IS '标签 URL 友好标识符 (必须唯一，如 "java", "spring-boot")';
COMMENT ON COLUMN blog_tags.post_count IS '文章数, 用于统计标签下文章数';
COMMENT ON COLUMN blog_tags.description IS '标签描述';
COMMENT ON COLUMN blog_tags.create_time IS '创建时间';
COMMENT ON COLUMN blog_tags.update_time IS '最后更新时间';

CREATE UNIQUE INDEX idx_tag_name ON blog_tags (name);
CREATE UNIQUE INDEX idx_tag_slug ON blog_tags (slug);

-- 删除并创建文章表
DROP TABLE IF EXISTS blog_article CASCADE;
CREATE TABLE blog_article
(
    id             BIGINT PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    slug           VARCHAR(255) NOT NULL,
    content        TEXT         NOT NULL,
    summary        TEXT                  DEFAULT NULL,
    cover_image    VARCHAR(255)          DEFAULT NULL,
    status         SMALLINT              DEFAULT 0,
    category_id    BIGINT       NOT NULL,
    author_id      BIGINT       NOT NULL,
    is_top         SMALLINT              DEFAULT 0,
    is_recommend   SMALLINT              DEFAULT 0,
    is_commentable SMALLINT              DEFAULT 1,
    publish_at     TIMESTAMP             DEFAULT NULL,
    published_time TIMESTAMP             DEFAULT NULL,
    modified_time  TIMESTAMP             DEFAULT NULL,
    view_count     INT                   DEFAULT 0,
    like_count     INT                   DEFAULT 0,
    word_count     INT                   DEFAULT 0,
    read_time      VARCHAR(64)           DEFAULT NULL,
    create_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE blog_article IS '文章核心信息表';
COMMENT ON COLUMN blog_article.id IS '文章唯一标识符 (主键)';
COMMENT ON COLUMN blog_article.title IS '文章标题';
COMMENT ON COLUMN blog_article.slug IS '文章 URL 友好标识符 (必须唯一，用于生成永久链接)';
COMMENT ON COLUMN blog_article.content IS '文章详细内容 (支持 Markdown/HTML)';
COMMENT ON COLUMN blog_article.summary IS '文章摘要 (用于列表页预览)';
COMMENT ON COLUMN blog_article.cover_image IS '文章封面图片 URL';
COMMENT ON COLUMN blog_article.status IS '文章状态: draft(0草稿), published(1已发布), private(2私密)';
COMMENT ON COLUMN blog_article.category_id IS '外键，关联分类表 id (文章所属主分类)';
COMMENT ON COLUMN blog_article.author_id IS '外键，关联用户表 id (文章作者)';
COMMENT ON COLUMN blog_article.is_top IS '是否置顶: 1(是), 0(否)';
COMMENT ON COLUMN blog_article.is_recommend IS '是否推荐: 1(是), 0(否)';
COMMENT ON COLUMN blog_article.is_commentable IS '是否允许评论: 1(允许), 0(不允许)';
COMMENT ON COLUMN blog_article.publish_at IS '文章需要定时发布时设置';
COMMENT ON COLUMN blog_article.published_time IS '文章发布时间 (当状态变为 published 时设置)';
COMMENT ON COLUMN blog_article.modified_time IS '文章内容修改时间(当状态为 published 时, 每次修改时设置)';
COMMENT ON COLUMN blog_article.view_count IS '文章阅读次数';
COMMENT ON COLUMN blog_article.like_count IS '文章点赞次数';
COMMENT ON COLUMN blog_article.word_count IS '文章字数统计';
COMMENT ON COLUMN blog_article.read_time IS '预计阅读时间';
COMMENT ON COLUMN blog_article.create_time IS '记录创建时间';
COMMENT ON COLUMN blog_article.update_time IS '记录最后更新时间';

CREATE UNIQUE INDEX idx_article_slug ON blog_article (slug);

-- 删除并创建文章标签关联表
DROP TABLE IF EXISTS rel_article_tag CASCADE;
CREATE TABLE rel_article_tag
(
    id          BIGINT PRIMARY KEY,
    article_id  BIGINT NOT NULL,
    tag_id      BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE rel_article_tag IS '文章标签关联表';
COMMENT ON COLUMN rel_article_tag.id IS '主键ID';
COMMENT ON COLUMN rel_article_tag.article_id IS '文章ID';
COMMENT ON COLUMN rel_article_tag.tag_id IS '标签ID';
COMMENT ON COLUMN rel_article_tag.create_time IS '创建时间';
COMMENT ON COLUMN rel_article_tag.update_time IS '更新时间';

-- 删除并创建系统令牌表
DROP TABLE IF EXISTS sys_token CASCADE;
CREATE TABLE sys_token
(
    id                 BIGINT           NOT NULL PRIMARY KEY,
    access_token       VARCHAR(512)     NOT NULL,
    refresh_token      VARCHAR(512),
    token_style VARCHAR(32) NOT NULL,
    user_id            BIGINT           NOT NULL,
    issued_at          TIMESTAMP        NOT NULL,
    expired_at         TIMESTAMP        NOT NULL,
    refresh_expired_at TIMESTAMP                 DEFAULT NULL,
    revoked            SMALLINT                  DEFAULT 0,
    revoked_at         TIMESTAMP                 DEFAULT NULL,
    ip_address         VARCHAR(45)               DEFAULT NULL,
    additional_info    JSON                      DEFAULT NULL,
    create_time        TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time        TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_token IS '系统令牌表';
COMMENT ON COLUMN sys_token.id IS '主键ID';
COMMENT ON COLUMN sys_token.access_token IS '访问令牌字符串';
COMMENT ON COLUMN sys_token.refresh_token IS '刷新令牌字符串';
COMMENT ON COLUMN sys_token.token_style IS '令牌风格';
COMMENT ON COLUMN sys_token.user_id IS '关联用户ID';
COMMENT ON COLUMN sys_token.issued_at IS '颁发时间';
COMMENT ON COLUMN sys_token.expired_at IS '过期时间';
COMMENT ON COLUMN sys_token.refresh_expired_at IS '刷新令牌过期时间';
COMMENT ON COLUMN sys_token.revoked IS '是否已撤销（1-是，0-否）';
COMMENT ON COLUMN sys_token.revoked_at IS '撤销时间';
COMMENT ON COLUMN sys_token.ip_address IS '客户端IP';
COMMENT ON COLUMN sys_token.additional_info IS '扩展信息（JSON格式）';
COMMENT ON COLUMN sys_token.create_time IS '记录创建时间';
COMMENT ON COLUMN sys_token.update_time IS '记录最后更新时间';

CREATE UNIQUE INDEX uk_access_token ON sys_token (access_token);
CREATE UNIQUE INDEX uk_refresh_token ON sys_token (refresh_token);

-- 删除并创建系统区域表
DROP TABLE IF EXISTS sys_region CASCADE;
CREATE TABLE sys_region
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code  VARCHAR(32) NOT NULL,
    name  VARCHAR(64) NOT NULL,
    pcode VARCHAR(32),
    level INT         NOT NULL
);

COMMENT ON TABLE sys_region IS '系统区域表';
COMMENT ON COLUMN sys_region.id IS '主键ID';
COMMENT ON COLUMN sys_region.code IS '编码';
COMMENT ON COLUMN sys_region.name IS '名称';
COMMENT ON COLUMN sys_region.pcode IS '父级编码';
COMMENT ON COLUMN sys_region.level IS '区域类型;1-省级,2-市级,3-区县级,4-乡镇级,5-村级';

CREATE UNIQUE INDEX uk_region_code ON sys_region (code);

-- 创建触发器函数用于自动更新时间戳
-- CREATE OR REPLACE FUNCTION update_updated_at_column()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     NEW.update_time = NOW();
--     RETURN NEW;
-- END;
-- $$ language 'plpgsql';
--
-- -- 为需要自动更新时间戳的表创建触发器
-- CREATE TRIGGER update_sys_role_updated_at BEFORE UPDATE ON sys_role FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_sys_permission_updated_at BEFORE UPDATE ON sys_permission FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_sys_user_updated_at BEFORE UPDATE ON sys_user FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_sys_user_role_updated_at BEFORE UPDATE ON sys_user_role FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_sys_role_permission_updated_at BEFORE UPDATE ON sys_role_permission FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_blog_category_updated_at BEFORE UPDATE ON blog_category FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_blog_tags_updated_at BEFORE UPDATE ON blog_tags FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_blog_article_updated_at BEFORE UPDATE ON blog_article FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_rel_article_tag_updated_at BEFORE UPDATE ON rel_article_tag FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_sys_token_updated_at BEFORE UPDATE ON sys_token FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- CREATE TRIGGER update_sys_region_updated_at BEFORE UPDATE ON sys_region FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

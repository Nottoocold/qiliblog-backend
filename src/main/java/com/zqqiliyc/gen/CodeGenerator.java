package com.zqqiliyc.gen;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.zqqiliyc.domain.entity.BaseEntity;

import java.sql.Types;
import java.util.Collections;

/**
 * @author zqqiliyc
 * @since 2025-03-25
 */
public class CodeGenerator {
    static final String url = "jdbc:mysql://localhost:3306/qiliblog?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    static final String username = "qiliblog";
    static final String password = "qiliblog001";

    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("qili")
                            .disableOpenDir()
                            .outputDir("gen"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT || typeCode == Types.TINYINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.zqqiliyc") // 设置父包名
                                //.moduleName("test") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "gen/mapper")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.entityBuilder()
                                .enableFileOverride()
                                .superClass(BaseEntity.class) // 设置父类
                                .addSuperEntityColumns("id", "create_time", "update_time", "del_flag")
                                .logicDeleteColumnName("del_flag") // 逻辑删除字段名
                                .idType(IdType.ASSIGN_ID)
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

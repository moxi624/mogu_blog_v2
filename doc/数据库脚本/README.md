## 数据库文件说明

## 目录

- mogu_blog.sql：代表mogu_blog数据库的文件
- mogu_blog_update.sql：代表mogu_blog在后续开发时候更新的字段）
- mogu_picture.sql：代表mogu_picture数据库文件
- mogu_picture_update.sql：代表mogu_picture在后续开发时候更新的字段）

## 注意

首次导入数据库文件的时候，我们只需要执行mogu_blog.sql 和 mogu_picture.sql文件即可，如果你在之前已经部署了本项目，那么你需要在对应的update.sql文件中，打开后，从中找到没有的字段，复制上执行即可

里面每个字段的添加，都会有对应的日期提示，如果有些字段是你clone项目后添加的，那么你就需要执行它们一遍即可

同时设置数据库访问账户和密码为： admin  admin，当然不设置也没关系，就是后面修改yml文件里面的配置即可
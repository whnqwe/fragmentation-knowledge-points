# mysql自增id主键操大问题查询



> 查看某张表下一个自增主键的值

```mysql
SELECT
	AUTO_INCREMENT
FROM
	INFORMATION_SCHEMA. TABLES
WHERE
	TABLE_NAME = 'table_name'
```



#### 思考

> 表中实际数据行与自增主键下一个值不符
>
> > 实际有100w   
> >
> > 下一个自增值: 1000w



> 表中有大量的删除插入操作?
>
> 
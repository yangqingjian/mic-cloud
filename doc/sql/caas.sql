
/*==============================================================*/
/* Table: caas_code_rule                                        */
/*==============================================================*/
create table caas_code_rule
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   code                 varchar(255) comment '编码',
   date_format          varchar(255) comment '日期格式（YYYYMMDD，YYMMDD，YYYYMM,YYMM等）如果没有就不填写',
   prefix               varchar(255) comment '编码前缀',
   seq_length           int(10) comment '顺序号的长度',
   length               int(10) comment '编码的总长度',
   step_length          int(10) comment '步长',
   start_number         bigint(20) comment '开始序号',
   period               varchar(255) comment '周期(年/月/日)',
   remark               varchar(255) comment '备注',
   bizkey_front_date_flag bit(1) comment 'BIZKEY在DATE的前面（默认为否）',
   primary key (id)
);

alter table caas_code_rule comment '编码规则';

/*==============================================================*/
/* Index: idx_code                                              */
/*==============================================================*/
create index idx_code on caas_code_rule
(
   code
);

/*==============================================================*/
/* Table: caas_code_series_number                               */
/*==============================================================*/
create table caas_code_series_number
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   code_rule_id         varchar(255) comment '编码配置ID',
   date_str             varchar(255) comment '格式化后的日期',
   seq_number           bigint(20) comment '当前顺序号',
   biz_key              varchar(255) comment '业务变量',
   primary key (id)
);

alter table caas_code_series_number comment '编码序列号';

/*==============================================================*/
/* Index: idx_code_rule_id                                      */
/*==============================================================*/
create index idx_code_rule_id on caas_code_series_number
(
   code_rule_id
);

/*==============================================================*/
/* Table: caas_org_pos                                          */
/*==============================================================*/
create table caas_org_pos
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   name                 varchar(255) comment '职位名称',
   remark               varchar(512) comment '职位描述',
   org_id               varchar(255) comment '组织ID',
   pos_id               varchar(255) comment '岗位ID',
   limit_number         int(5) comment '人数限制',
   start_date           datetime comment '开始日期',
   end_date             datetime comment '结束日期',
   primary key (id)
);

alter table caas_org_pos comment '职位';

/*==============================================================*/
/* Table: caas_data_dictionary                                  */
/*==============================================================*/
create table caas_data_dictionary
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   name                 varchar(255) comment '名称',
   code                 varchar(255) comment '编码',
   data_value           varchar(255) comment '数据值',
   remark               varchar(255) comment '备注',
   parent_id            varchar(255) comment '父节点',
   enabled_flag         bit(1) comment '是否开启',
   sort_number          int(11) comment '排序',
   primary key (id)
);

alter table caas_data_dictionary comment '系统字典';

/*==============================================================*/
/* Table: caas_emp_role_rel                                     */
/*==============================================================*/
create table caas_emp_role_rel
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   emp_id               varchar(255) comment '员工id',
   role_id              varchar(255) comment '角色id',
   primary key (id)
);

alter table caas_emp_role_rel comment '员工额外角色';

/*==============================================================*/
/* Table: caas_emp_service_info                                 */
/*==============================================================*/
create table caas_emp_service_info
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   emp_id               varchar(255) comment '员工ID',
   org_pos_id           varchar(255) comment '职位ID',
   start_date           datetime comment '开始日期',
   end_date             datetime comment '结束日期',
   leader_emp_id        varchar(255) comment '上级领导',
   primary key (id)
);

alter table caas_emp_service_info comment '员工任职记录';

/*==============================================================*/
/* Table: caas_employee                                         */
/*==============================================================*/
create table caas_employee
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   name                 varchar(255) comment '姓名',
   mobile               varchar(255) comment '手机号',
   id_card              varchar(255) comment '身份证',
   birthday             datetime comment '生日',
   primary_dept_pos_id  varchar(255) comment '主职位ID',
   sex                  varchar(255) comment '性别',
   head_icon            varchar(255) comment '头像',
   primary key (id)
);

alter table caas_employee comment '员工表';

/*==============================================================*/
/* Table: caas_login_log                                        */
/*==============================================================*/
create table caas_login_log
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   type                 varchar(255) comment '用户类型',
   login_code           varchar(255) comment '登录名',
   login_name           varchar(255) comment '用户名称',
   login_secret         varchar(255) comment '登录密钥',
   ip                   varchar(255) comment '登录IP',
   equipment            varchar(1024) comment '设备',
   success_flag         int(1) comment '是否成功',
   primary key (id)
);

alter table caas_login_log comment '登录日志';

/*==============================================================*/
/* Table: caas_organization                                     */
/*==============================================================*/
create table caas_organization
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   name                 varchar(255) comment '组织名称',
   code                 varchar(255) comment '组织编码',
   type                 varchar(255) comment '组织类型',
   head_emp             varchar(255) comment '组织负责人',
   parent_id            varchar(255) comment '父级组织',
   level                int(5) comment '层级',
   path_name_str        varchar(1024) comment '名称层级',
   path_ids             varchar(1024) comment 'id层级',
   short_name           varchar(255) comment '简称',
   address              varchar(512) comment '地址',
   primary key (id)
);

alter table caas_organization comment '组织';

/*==============================================================*/
/* Table: caas_pos_role_rel                                     */
/*==============================================================*/
create table caas_pos_role_rel
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   pos_id               varchar(255) comment '岗位ID',
   role_id              varchar(255) comment '角色ID',
   primary key (id)
);

alter table caas_pos_role_rel comment '岗位和角色关系表';

/*==============================================================*/
/* Table: caas_position                                         */
/*==============================================================*/
create table caas_position
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   name                 varchar(255) comment '名称',
   remark               varchar(512) comment '描述',
   code                 varchar(255) comment '编码',
   start_date           datetime comment '开始日期',
   end_date             datetime comment '结束日期',
   primary key (id)
);

alter table caas_position comment '岗位';

/*==============================================================*/
/* Table: caas_region                                           */
/*==============================================================*/
create table caas_region
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   region_code          varchar(255) comment '编码',
   region_name          varchar(255) comment '名称',
   parent_code          varchar(255) comment '父区域编码',
   primary key (id)
);

alter table caas_region comment '行政区域';

/*==============================================================*/
/* Table: caas_resource                                         */
/*==============================================================*/
create table caas_resource
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   name                 varchar(255) comment '资源名称',
   code                 varchar(255) comment '资源编码',
   remark               varchar(512) comment '资源描述',
   type                 varchar(255) comment '资源类型',
   parent_id            varchar(255) comment '父类id',
   level                int(5) comment '层级',
   path_name_str        varchar(1024) comment '名称层级',
   path_ids             varchar(1024) comment 'id层级',
   primary key (id)
);

alter table caas_resource comment '资源表';

/*==============================================================*/
/* Table: caas_role                                             */
/*==============================================================*/
create table caas_role
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   name                 varchar(255) comment '名称',
   remark               varchar(512) comment '描述',
   code                 varchar(255) comment '编码',
   primary key (id)
);

alter table caas_role comment '角色';

/*==============================================================*/
/* Table: caas_role_res_rel                                     */
/*==============================================================*/
create table caas_role_res_rel
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   role_id              varchar(255) comment '角色ID',
   res_id               varchar(255) comment '资源ID',
   primary key (id)
);

alter table caas_role_res_rel comment '角色和资源关系';

/*==============================================================*/
/* Table: caas_user_auth                                        */
/*==============================================================*/
create table caas_user_auth
(
   id                   varchar(255) not null comment '主键id',
   created_time         datetime comment '创建时间',
   created_by           varchar(255) comment '创建人',
   last_updated_time    datetime comment '最后修改时间',
   modified_by          varchar(255) comment '最后修改人',
   is_deleted           int(1) default 0 comment '软删除标志',
   type                 varchar(255) comment '用户类型',
   login_code           varchar(255) comment '登录名',
   login_name           varchar(255) comment '用户名称',
   login_secret         varchar(255) comment '登录密钥',
   primary key (id)
);

alter table caas_user_auth comment '用户授权表';

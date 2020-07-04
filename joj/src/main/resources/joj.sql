/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/6/10 17:27:26                           */
/*==============================================================*/
drop database if exists joj;

create database joj;

use joj;

drop table if exists joj_announcement;

drop table if exists joj_blog;

drop table if exists joj_blog_comment;

drop table if exists joj_blog_type;

drop table if exists joj_calendar_contest;

drop table if exists joj_contest;

drop table if exists joj_contest_board;

drop table if exists joj_contest_comment;

drop table if exists joj_contest_problem;

drop table if exists joj_description;

drop table if exists joj_message;

drop table if exists joj_oj_daily_board;

drop table if exists joj_participate;

drop table if exists joj_problem;

drop table if exists joj_problem_tag;

drop table if exists joj_problem_tags;

drop table if exists joj_resource;

drop table if exists joj_role;

drop table if exists joj_role_resource;

drop table if exists joj_statistics;

drop table if exists joj_submission;

drop table if exists joj_user;

drop table if exists joj_user_daily_board;

drop table if exists joj_user_rank;

/*==============================================================*/
/* Table: joj_announcement                                      */
/*==============================================================*/
create table joj_announcement
(
    aid        int not null auto_increment,
    uid        int,
    title      nvarchar(200),
    content    text,
    createTime datetime,
    updateTime datetime,
    primary key (aid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_blog                                              */
/*==============================================================*/
create table joj_blog
(
    bid          int not null auto_increment,
    uid          int not null,
    btid         int,
    title        nvarchar(200),
    content      text,
    summary      text,
    firstPicture nvarchar(300),
    views        int,
    createTime   datetime,
    updateTime   datetime,
    published    bool,
    comment      bool,
    primary key (bid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_blog_comment                                      */
/*==============================================================*/
create table joj_blog_comment
(
    bcid            int not null auto_increment,
    bid             int,
    replyCommentId  int default null,
    parentCommentId int default null,
    uid             int not null,
    content         text,
    createTime      datetime,
    email           varchar(100),
    remind          bool,
    primary key (bcid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_blog_type                                         */
/*==============================================================*/
create table joj_blog_type
(
    btid     int not null auto_increment,
    typeName nvarchar(100),
    primary key (btid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_calendar_competitions                             */
/*==============================================================*/
create table joj_calendar_competitions
(
    oid       int not null auto_increment,
    OJName    nvarchar(100),
    title     nvarchar(200),
    beginTime datetime,
    endTime   datetime,
    url       varchar(200),
    primary key (oid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_contest                                           */
/*==============================================================*/
create table joj_contest
(
    cid          int not null auto_increment,
    uid          int not null,
    title        nvarchar(200),
    description  nvarchar(3000),
    announcement nvarchar(1000),
    password     varchar(30),
    beginTime    datetime,
    endTime      datetime,
    auth         int,
    type         int,
    primary key (cid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_contest_board                                     */
/*==============================================================*/
create table joj_contest_board
(
    cbid int not null auto_increment,
    sid  int not null,
    cid  int,
    primary key (cbid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_contest_comment                                   */
/*==============================================================*/
create table joj_contest_comment
(
    ccid            int not null auto_increment,
    replyCommentId  int default null,
    uid             int,
    parentCommentId int default null,
    cid             int,
    content         text,
    createTime      datetime,
    primary key (ccid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_contest_problem                                   */
/*==============================================================*/
create table joj_contest_problem
(
    cpid  int not null auto_increment,
    pid   int not null,
    cid   int,
    title nvarchar(200),
    num   varchar(3),
    primary key (cpid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_description                                       */
/*==============================================================*/
create table joj_description
(
    did         int not null auto_increment,
    uid         int not null,
    pid         int not null,
    description text,
    input       text,
    output      text,
    samples     text,
    hint        text,
    updateTime  datetime,
    author      nvarchar(30),
    remarks     text,
    primary key (did)
) charset = utf8;

/*==============================================================*/
/* Table: joj_message                                           */
/*==============================================================*/
create table joj_message
(
    mid             int          not null auto_increment,
    replyMessageId  int default null,
    parentMessageId int default null,
    uid             int,
    createTime      datetime,
    content         text,
    email           varchar(100) not null,
    userName        varchar(30)  not null,
    remind          bool,
    avatar          varchar(200),
    primary key (mid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_oj_daily_board                                    */
/*==============================================================*/
create table joj_oj_daily_board
(
    odbid       int not null auto_increment,
    submitCount int default 0,
    accepted    int default 0,
    collectTime date,
    primary key (odbid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_participate                                       */
/*==============================================================*/
create table joj_participate
(
    uid int not null,
    cid int not null,
    primary key (uid, cid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_problem                                           */
/*==============================================================*/
create table joj_problem
(
    pid         int not null auto_increment,
    uid         int not null,
    title       nvarchar(200),
    source      nvarchar(300),
    url         varchar(200),
    originOJ    nvarchar(300),
    originProb  nvarchar(100),
    memoryLimit int,
    timeLimit   int,
    updateTime  datetime,
    primary key (pid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_problem_tag                                       */
/*==============================================================*/
create table joj_problem_tag
(
    id      int not null auto_increment,
    tagName nvarchar(100) unique,
    primary key (id)
) charset = utf8;

/*==============================================================*/
/* Table: joj_problem_tags                                      */
/*==============================================================*/
create table joj_problem_tags
(
    id  int not null,
    pid int not null,
    primary key (id, pid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_resource                                          */
/*==============================================================*/
create table joj_resource
(
    resourceId   int not null auto_increment,
    resourceName nvarchar(200),
    primary key (resourceId)
) charset = utf8;

/*==============================================================*/
/* Table: joj_role                                              */
/*==============================================================*/
create table joj_role
(
    roleId   int not null auto_increment,
    roleName nvarchar(100),
    primary key (roleId)
) charset = utf8;

/*==============================================================*/
/* Table: joj_role_resource                                     */
/*==============================================================*/
create table joj_role_resource
(
    roleId     int not null,
    resourceId int not null,
    primary key (roleId, resourceId)
) charset = utf8;

/*==============================================================*/
/* Table: joj_statistics                                        */
/*==============================================================*/
create table joj_statistics
(
    psid int not null auto_increment,
    pid  int not null,
    AC   int,
    PE   int,
    WA   int,
    TLE  int,
    MLE  int,
    OLE  int,
    RE   int,
    CE   int,
    UE   int,
    primary key (psid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_submission                                        */
/*==============================================================*/
create table joj_submission
(
    sid             int not null auto_increment,
    uid             int not null,
    pid             int,
    status          varchar(30),
    statusType      int,
    additionalInfo  nvarchar(1000),
    realRunId       varchar(100),
    time            int,
    memory          int,
    subTime         datetime,
    language        varchar(10),
    displayLanguage varchar(30),
    sourceCode      text,
    primary key (sid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_user                                              */
/*==============================================================*/
create table joj_user
(
    uid        int not null auto_increment,
    userName   varchar(30) unique,
    nickName   nvarchar(30),
    password   varchar(30),
    email      varchar(100) unique,
    qq         varchar(15),
    avatar     varchar(200),
    createTime datetime,
    updateTime datetime,
    primary key (uid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_user_daily_board                                  */
/*==============================================================*/
create table joj_user_daily_board
(
    udbid      int not null auto_increment,
    uid         int,
    submitCount int default 0,
    accepted int default 0,
    collectTime date,
    primary key (udbid)
) charset = utf8;

/*==============================================================*/
/* Table: joj_user_rank                                         */
/*==============================================================*/
create table joj_user_rank
(
    urid     int not null auto_increment,
    uid      int not null,
    submit   int default 0,
    accepted int default 0,
    primary key (urid)
) charset = utf8;


create table joj_user_role
(
    uid                  int not null,
    roleId               int not null,
    primary key (uid, roleId)
);

alter table joj_announcement
    add constraint FK_Relationship_21 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_blog
    add constraint FK_Relationship_3 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_blog
    add constraint FK_Relationship_4 foreign key (typeId)
        references joj_blog_type (btid) on delete cascade on update cascade;

alter table joj_blog_comment
    add constraint FK_Relationship_25 foreign key (replyCommentId)
        references joj_blog_comment (bcid) on delete cascade on update cascade;

alter table joj_blog_comment
    add constraint FK_Relationship_26 foreign key (parentCommentId)
        references joj_blog_comment (bcid) on delete cascade on update cascade;

alter table joj_blog_comment
    add constraint FK_Relationship_29 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_blog_comment
    add constraint FK_Relationship_5 foreign key (bid)
        references joj_blog (bid) on delete cascade on update cascade;

alter table joj_contest
    add constraint FK_Relationship_12 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_contest_board
    add constraint FK_Relationship_17 foreign key (cid)
        references joj_contest (cid) on delete cascade on update cascade;


alter table joj_contest_board
    add constraint FK_Relationship_19 foreign key (sid)
        references joj_submission (sid) on delete cascade on update cascade;

alter table joj_contest_comment
    add constraint FK_Relationship_13 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_contest_comment
    add constraint FK_Relationship_27 foreign key (parentCommentId)
        references joj_contest_comment (ccid) on delete cascade on update cascade;

alter table joj_contest_comment
    add constraint FK_Relationship_28 foreign key (replyCommentId)
        references joj_contest_comment (ccid) on delete cascade on update cascade;

alter table joj_contest_comment
    add constraint FK_Relationship_7 foreign key (cid)
        references joj_contest (cid) on delete cascade on update cascade;

alter table joj_contest_problem
    add constraint FK_Relationship_23 foreign key (pid)
        references joj_problem (pid) on delete cascade on update cascade;

alter table joj_contest_problem
    add constraint FK_Relationship_24 foreign key (cid)
        references joj_contest (cid) on delete cascade on update cascade;

alter table joj_description
    add constraint FK_Relationship_15 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_description
    add constraint FK_Relationship_8 foreign key (pid)
        references joj_problem (pid) on delete cascade on update cascade;

alter table joj_message
    add constraint FK_Relationship_1 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_message
    add constraint FK_Relationship_30 foreign key (parentMessageId)
        references joj_message (mid) on delete cascade on update cascade;

alter table joj_message
    add constraint FK_Relationship_31 foreign key (replyMessageId)
        references joj_message (mid) on delete cascade on update cascade;

alter table joj_participate
    add constraint FK_joj_participate foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_participate
    add constraint FK_joj_participate2 foreign key (cid)
        references joj_contest (cid) on delete cascade on update cascade;

alter table joj_problem
    add constraint FK_Relationship_32 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_problem_tags
    add constraint FK_joj_problem_tags foreign key (id)
        references joj_problem_tag (id) on delete cascade on update cascade;

alter table joj_problem_tags
    add constraint FK_joj_problem_tags2 foreign key (pid)
        references joj_problem (pid) on delete cascade on update cascade;


alter table joj_role_resource
    add constraint FK_joj_role_resource foreign key (roleId)
        references joj_role (roleId) on delete cascade on update cascade;

alter table joj_role_resource
    add constraint FK_joj_role_resource2 foreign key (resourceId)
        references joj_resource (resourceId) on delete cascade on update cascade;

alter table joj_statistics
    add constraint FK_Relationship_14 foreign key (pid)
        references joj_problem (pid) on delete cascade on update cascade;

alter table joj_submission
    add constraint FK_Relationship_10 foreign key (pid)
        references joj_problem (pid) on delete cascade on update cascade;

alter table joj_submission
    add constraint FK_Relationship_16 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_user_daily_board
    add constraint FK_Relationship_22 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;

alter table joj_user_rank
    add constraint FK_Relationship_20 foreign key (uid)
        references joj_user (uid) on delete cascade on update cascade;


alter table joj_user_role add constraint FK_joj_user_role foreign key (uid)
    references joj_user (uid) on delete cascade on update cascade ;

alter table joj_user_role add constraint FK_joj_user_role2 foreign key (roleId)
    references joj_role (roleId) on delete cascade on update cascade ;
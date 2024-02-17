-- auto-generated definition
create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    username      varchar(256)                       null comment 'username',
    avatar_url    varchar(1024)                      null comment 'avatar',
    gender        tinyint                            null comment 'gender',
    password      varchar(512)                       null comment 'password',
    phone         varchar(128)                       null comment 'phone',
    email         varchar(512)                       null comment 'email',
    user_status   int      default 0                 null comment 'user_status',
    is_delete     tinyint  default 0                 null comment 'is_delete',
    user_role     int      default 0                 not null comment 'user_role，0-user，1-admin',
    id_code       varchar(512)                       null comment 'id_code'
)
    comment 'user';


DROP TABLE IF EXISTS Users;
create table if not exists Users
(
    ID       int         not null
        primary key,
    USERNAME varchar(45) not null,
    PASSWORD varchar(45) not null,
    EMAIL    varchar(45) not null,
    constraint EMAIL_UNIQUE
        unique (EMAIL),
    constraint ID_UNIQUE
        unique (ID),
    constraint USERNAME_UNIQUE
        unique (USERNAME)
);

create table if not exists FFMPEG_PROCESS
(
    id     VARCHAR(64) not null
        primary key,
    config LONGVARCHAR(4096),
    advanced_config LONGVARCHAR(4096)
);

Open-source проект онлайн-магазина.

http://hostedredmine.com/projects/online-store-opensource

Миграция базы настроена через flyway. sql скрипты располагаются в каталоге main/resources/db/migration
Для запуска миграции необходимо выполнить mvn compile properties:read-project-properties flyway:migrate
mvn compile properties:read-project-properties flyway:info - посмотреть информацию о состоянии версии базы
mvn compile properties:read-project-properties flyway:clean - очистить базу

Версия PostgreSQL Version 9.5.1

docker run -itd \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-p 5432:5432 \
-v /data:/var/lib/postgresql/data \
--name postgresql postgres

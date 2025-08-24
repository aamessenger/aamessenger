# Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

#!/bin/bash
set -e  # Exit on any error

# Start PostgreSQL in the background to perform initialization
gosu postgres pg_ctl -D "$PGDATA" -w start

# Create a user and database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER myuser WITH PASSWORD 'mypassword';
    CREATE DATABASE mydb OWNER myuser;
    GRANT ALL PRIVILEGES ON DATABASE mydb TO myuser;
EOSQL

# Execute custom SQL files (if any)
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "mydb" -f 02-sample-data.sql

# Stop PostgreSQL after initialization
gosu postgres pg_ctl -D "$PGDATA" stop

# Allow container to start normally via the standard entrypoint
exec "$@"
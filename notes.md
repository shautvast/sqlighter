**compile sqlite amalgamation for debug**

`gcc shell.c sqlite3.c -lpthread -ldl -lm \
-DSQLITE_DEBUG \
-DSQLITE_ENABLE_EXPLAIN_COMMENTS \
-DSQLITE_ENABLE_TREETRACE \
-DSQLITE_ENABLE_WHERETRACE \
-o sqlite3` 
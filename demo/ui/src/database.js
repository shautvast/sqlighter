import sqlite3InitModule from "sqlite-wasm-esm";

(async () => {
    let db;

    const error = (...args) => console.log('error', ...args);
    let sqlite3 = await sqlite3InitModule({printErr: error,});

    const dataBuffer = await fetch("/api/db/customers");
    try {
        // save the response in opfs:my.db
        const root = await navigator.storage.getDirectory();
        const draftFile = await root.getFileHandle("my.db", {create: true});
        const accessHandle = await draftFile.createSyncAccessHandle();
        accessHandle.write(new Uint8Array(await dataBuffer.arrayBuffer()));

        // release the lock, so the database can read the file
        await accessHandle.close();

        // open database using opfs and my.db
        db = new sqlite3.opfs.OpfsDb('my.db');
        postMessage("db ready");
    } catch (error) {
        // getting the accessHandle may fail if there are other locks
        console.log(error);
    }

    // listens to events from main thread
    onmessage = (e) => {
        let data = [];

        db.exec({
            sql: e.data, // the query
            callback: function (row) {
                // fetch the data, row by row
                // row is [value1, value2, ...]
                data.push(row);
            }
        });

        // pass the data as a message to the main thread
        postMessage(data);
    }
})();
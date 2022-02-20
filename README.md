# Wordle Sync for Android
The statistics for Wordle games are located in the browser's localStorage, but there's no way to sync those values across devices. This app saves and loads the values from a Firebase database, and can be used with [this chrome extension](https://github.com/whitnotmax/wordle-sync-extension) to sync your Wordle statistics across your PC and phone.

Your stats are loaded from the database when the Wordle website loads if you are logged in and you have saved your stats before; to save your stats, click the "Sync scores" button.

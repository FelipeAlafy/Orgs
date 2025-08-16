package net.felipealafy.orgs

import android.app.Application
import net.felipealafy.orgs.database.AppDatabase

class OrgsApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
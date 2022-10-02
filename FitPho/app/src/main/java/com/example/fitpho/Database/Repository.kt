package com.example.fitpho.Database

import androidx.lifecycle.LiveData

class Repository(private val guideDao: GuideDao) {
    val readAllData: LiveData<List<GuideEntity>> = guideDao.getAllGuide()

}
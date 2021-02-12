package com.timelysoft.amore.errors


object  Errors {

    val errors_hashMap = hashMapOf(
        "VAL002ER" to "Уже существует",
        "VAL005ER" to "Уже оплачен",
        "CACHE002ER" to "Ключ не найден в кэш",
        "CACHE001ER" to "Версия не найдена в кэш",
        "VAL001ER" to "Не может быть null",
        "DATE001ER" to "Дата и время должны быть больше чем сейчас",
        "VAL006ER" to "Отказан",
        "INT001ER" to "Должен быть больше 0",
        "SYS001ER" to "Внутренняя ошибка сервера",
        "AUTH002ER" to "Отказано в доступе",
        "AUTH001ER" to "Не аутентифицирован",
        "SYS004ER" to "Недоступен",
        "SYS005ER" to "Недоступен, попробуйте позже",
        "PWD001ER" to "Потвержденный пароль не совпадает с паролем",
        "VAL003ER" to "Не найден",
        "ORDR001ER" to "Не поддерживается онлайн оплата",
        "SYS002ER" to "Ошибка сервера",
        "SYS003ER" to "Неизвестная ошибка",
        "VAL004ER" to "Неправильные данные"
    )
    val properties = hashMapOf(
        "cankInfo" to "Банковская информация",
                "Base" to "base",
        "callCenter" to "Колл-центр",
        "carbis"  to "Карбис",
        "check" to "Чек",
        "city" to "Город",
        "currency" to "Валюта",
        "externalClient" to "Внешний клиент",
        "file" to "Файл",
        "ignore" to "Не учитывать",
        "Info" to "Информация",
        "link" to "Ссылка",
        "loginOrPassword" to "Логин или пароль",
        "order" to "Заказ",
        "payment" to "Оплата",
        "paymentConfirmation" to "Подтверждение оплаты",
        "rawText" to "Необработанный текст",
        "restaurant" to "Ресторан",
        "restaurantGroup" to "Ресторанная группа",
        "setting" to "Настройки",
        "street" to "Улица",
        "text" to "Текст",
        "user" to "Пользователь"
    )

}
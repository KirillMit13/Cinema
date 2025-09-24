## SkillCinema (Кинопоиск клиент)

Современное Android‑приложение на Kotlin и Jetpack Compose для просмотра фильмов и сериалов с Кинопоиск API: главная лента, поиск, детальные карточки, похожие и галерея, сезоны, истории просмотров и пользовательские коллекции (включая «Любимые» и «Хочу посмотреть»).


### Стек технологий
- **Язык**: Kotlin, Coroutines/Flow
- **UI**: Jetpack Compose, Material 3, Navigation Compose, Lottie, custom neon UI
- **Загрузка изображений**: Coil (есть Glide в зависимостях)
- **Данные**: Room (DAO/Entities), DataStore Preferences
- **Сеть**: Retrofit2, OkHttp3 Logging Interceptor
- **Пагинация**: Paging 3 (runtime + compose)
- **Архитектура**: MVVM, Repository, ViewModelFactory
- **Прочее**: Firebase Crashlytics/Analytics (подключается при наличии `google-services.json`), RecyclerView (в проекте, в основном Compose)

### Функционал
- **Главная**: тематические секции (Популярное, Топ‑250, Сериалы, Боевики США) с переходом к полным спискам
- **Поиск**: дебаунс, отображение результатов, переход к деталям фильма
- **Детали фильма**: описание, рейтинг, актёрский состав, похожие фильмы, изображения, сезоны/эпизоды
- **Галерея**: экран превью и просмотр изображений в полноэкранном режиме
- **Коллекции**: системные и кастомные, создание/удаление коллекций, добавление/удаление фильмов, тумблеры «Любимые»/«Хочу посмотреть», отметка «Просмотрено»
- **История**: список последних просмотренных карточек
- **Профиль**: быстрый доступ к избранному/коллекциям/истории
- **Онбординг**: первичная настройка с сохранением статуса в DataStore
- **Тёмная/светлая тема**: автоматический выбор, неоновая палитра

### Установка и запуск
1. Клонировать репозиторий
   ```bash
   git clone <repo-url>
   cd final_android_lvl1
   ```
2. Открыть в Android Studio (Giraffe+), дать IDE синхронизировать Gradle
3. Указать API‑ключ Кинопоиска (неофициальный API). Добавьте строку в `~/.gradle/gradle.properties` или в корневой `gradle.properties`/`local.properties` проекта:
   ```
   KINOPOISK_API_KEY=ВАШ_API_КЛЮЧ
   ```
   Ключ автоматически прокинется в `BuildConfig.KINOPOISK_API_KEY` и будет использован интерсептором `ApiKeyInterceptor`.
4. Запустить приложение на эмуляторе/устройстве (minSdk 24, targetSdk 34)

Если используете Crashlytics/Analytics — добавьте `google-services.json` и подключите плагин.

### Структура проекта
- Модуль: один модуль `app`
- Основные директории:
  - `app/src/main/java/com/example/cinema/`
    - Экраны и VM: `HomeScreen.kt`, `HomeViewModel.kt`, `SearchScreen.kt`, `SearchViewModel.kt`, `FilmDetailsScreen.kt`, `FilmDetailsViewModel.kt`, `CollectionsScreen.kt`, `CollectionsViewModel.kt`, `CollectionDetailsViewModel.kt`, `GalleryScreen.kt`, `GalleryViewModel.kt`, `SeasonsScreen.kt`, `SeasonsViewModel.kt`, `ProfileScreen.kt`, `ProfileViewModel.kt`, `HistoryScreen.kt`, `HistoryViewModel.kt`, `ActorScreen.kt`, `ActorViewModel.kt`
    - DI/инициализация: `AppContainer.kt`, `MainActivity.kt`, `SkillCinemaApp.kt`
    - Данные: Room (`AppDatabase.kt`, `FilmDao.kt`, `CollectionDao.kt`, `HistoryDao.kt`, `FilmEntity.kt`, `CollectionEntity.kt`, `FilmCollectionCrossRef.kt`, `HistoryEntity.kt`)
    - Сеть: `data/remote/ApiClient.kt`, `data/remote/model/KinopoiskService.kt` + модели/DTO
    - Мапперы: `data/mapper/*` (преобразование DTO → domain)
    - Домен: `domain/model/*` (чистые модели `Film`, `FilmDetails`, `Collection`, `Person`)
    - UI компоненты: `ui/components/*` (неоновая палитра, шимер, иконки, ошибки)
    - Прочее: `OnboardingPrefs.kt`, `SearchPrefs.kt`, `ApiKeyInterceptor.kt`
  - `app/src/main/res/` — ресурсы (строки, темы, иконки, макеты, изображения)

### ARCHITECTURE
- **Паттерн**: MVVM с разделением на слой данных (Remote/Local), доменную модель и презентацию (Compose + ViewModel). DI упрощён через `AppContainer`.
- **Организация пакетов**:
  - `data/remote` — Retrofit клиент, сервис `KinopoiskService`, DTO
  - `data/mapper` — преобразование в `domain.model`
  - корень `com.example.cinema` — экраны, VM, репозитории, DAO/Entity, контейнер
  - `domain/model` — бизнес‑модели UI/домена
  - `ui/components` — переиспользуемые Compose компоненты и тема
- **Где хранится бизнес‑логика**:
  - Основная бизнес‑логика в `ViewModel` и репозиториях (`HomeRepository`, `CollectionsRepository`, др.). VM инкапсулируют StateFlow, дебаунс поиска, загрузки/ошибки и преобразование данных для UI.
- **Доступ к данным**:
  - API: `Retrofit` → `ApiClient` с `OkHttp` и `ApiKeyInterceptor(BuildConfig.KINOPOISK_API_KEY)` → интерфейс `KinopoiskService` (эндпоинты: топы/премьеры/фильтры/поиск/детали/актёры/похожие/изображения/сезоны)
  - БД: `Room` (`AppDatabase`) с DAO: `FilmDao`, `CollectionDao`, `HistoryDao`; связи через `FilmCollectionCrossRef`; Observable данные возвращаются как `Flow`
  - Prefs: `DataStore Preferences` для онбординга и настроек поиска
  - Пагинация: `Paging 3` (экраны списков через `AllItemsPaging.kt`/`AllItemsPagingScreen`)


### Автор
- Имя: Митюшин Кирилл
- GitHub: https://github.com/KirillMit13



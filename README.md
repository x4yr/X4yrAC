# X4yrAC
![Downloads](https://img.shields.io/github/downloads/x4yr/X4yrAC/total?style=flat&logo=github&color=green)
![Forks](https://img.shields.io/github/forks/x4yr/X4yrAC?style=flat&logo=github&color=green)

![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Java 21](https://img.shields.io/badge/Java-21-red?logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.3.0-02303A?logo=gradle&logoColor=white)

### Информация:

**X4yrAC** — это **опен сурс античит** нового поколения, 
это **античит основанный на AI** в котором можно использовать **Hugging Face или SignalR модели античита**

Наш сайт: [ac.limetime.space](https://ac.limetime.space)

```
тг: @x4yr_tg
тг(бот): @x4yrac_bot
дс: xayrllano_77932
```

### Определение:
- **Датасет** - способ **сбора данных** одного или нескольких игроков, с возможностью позже использовать эти **данные** для обучение античита.

- **Легит** - игрок который не использует **Запрещенное ПО** для игры.

- **Читер** - игрок который использует **Запрещенное ПО** для игры.

- **Ложное Срабатывание** - ошибка античита или неправильной настройки модели, при которой античит может решить что **легитный игрок** являеться **читером**.

- **Детекты** - обнаружение функции **Запрещенного ПО** благодаря античиту.

****

## Глава I - Дата-Сеты:

Датасет — это коллекция данных, которую вы можете собрать с помощью античита, и использовать потом для обучение модели античита.

В X4yrAC используеться следущие данные для создание датасета:

```
Поле	        Тип	Описание
delta_yaw	float	Изменение угла поворота по горизонтали (влево-вправо)
delta_pitch	float	Изменение угла поворота по вертикали (вверх-вниз)
accel_yaw	float	Ускорение поворота по yaw: delta_yaw[t] - delta_yaw[t-1]
accel_pitch	float	Ускорение поворота по pitch: delta_pitch[t] - delta_pitch[t-1]
jerk_yaw	float	Рывок (3-я производная) по yaw: accel_yaw[t] - accel_yaw[t-1]
jerk_pitch	float	Рывок (3-я производная) по pitch: accel_pitch[t] - accel_pitch[t-1]
gcd_error_yaw	float	Ошибка GCD по yaw — отклонение от "шага мыши" игрока
gcd_error_pitch	float	Ошибка GCD по pitch — отклонение от "шага мыши" игрока

```
### Сбор датасетов:
##### Перед сбором датасетов:
Для сбора датасетов вам понадобиться 2 игрока (читер, легит) для хорошего датасета вам понадобиться двоем быть в движении при PvP и обучить от 40к строчек дата-сета, а также нужно делать несколько датасетов с разными видами PvP для легита и читера.

#### Для датасетов вы должны использовать следущие комманды:
```
/xac start (ник) (cheat/legit) (комментарий) - Начать обучение
/xac datastatus - Посмотреть информацию о текущем обучении
/xac stop (ник) - Завершить обучение
```
После обучение датасетов можете переходить к **Главе II**

****
## Глава II - Модель

#### Создание и обучение своей модели
В данном примере я реализовал простой Python скрипт создает модель на основе нескольких датасетов, вам нужно будет скачать данную программу: Pycharm (https://www.jetbrains.com/pycharm/), после установки создаете новый проект и класс main.py переименовываем на train.py и после чего вставляем туда скрипт с данного репозитория: https://github.com/x4yr/x4yrac-train далее создаете внутри проекта директории **datasets** и **model**, и внутри директории **datasets** создаете **cheat** и **legit**, внутри cheat вы должны закинуть датасеты которые вы обучали на читере, а внутри legit вы должны закинуть датасеты которые вы обучали на легите.

Далее вы должны открыть вкладку 
```
Package           Version
----------------- ----------
filelock          3.20.3
fsspec            2026.1.0
Jinja2            3.1.6
MarkupSafe        3.0.3
mpmath            1.3.0
networkx          3.6.1
numpy             2.4.1
pip               23.2.1
setuptools        80.10.2
sympy             1.14.0
torch             2.10.0+cpu
typing_extensions 4.15.0
```
Внизу в левом угле будет разные иконки, наведитесь на ту где высвечиваеться Terminal при наведении, и там пишите данные комманды по очереди:
```
pip install filelock 3.20.3
pip install fsspec 2026.1.0
pip install Jinja2 3.1.6
pip install MarkupSafe 3.0.3
pip install mpmath 1.3.0
pip install networkx 3.6.1
pip install numpy 2.4.1
pip install pip 23.2.1
pip install setuptools 80.10.2
pip install sympy 1.14.0
pip install torch 2.10.0+cpu
pip install typing_extensions 4.15.0
```
После выполнение каждой комманды, в том же Terminal пишите:
```
.venv\Scripts\python.exe train.py --cheat-dirs datasets/cheat --legit-dirs datasets/legit --output model
```

После у вас начнеться создание модели и в Terminal будут логи об этом, после того как модель будет создана в папке **model** вы увидите 2 файла, а именно **model_full.pt** и **pytorch_model.bin**, для удобства эти файлы можете переместить на рабочий стол, они нам скоро понадобяться.
****
## Глава III - Загрузка модели на Hugging Face

#### Подготовка
Для начало вы должно зарегестрироваться на сайте Hugging Face (https://huggingface.co/)
после успешной регистрации вы должно перейти сюда: https://huggingface.co/new-space

#### Создание Space
На этой странице вы должны заполнить следущие поля: 
```
Space Name - Название вашего Space, на английском
Short Description - Описание вашего Space, можно на руском
License - Здесь можете выбрать лицензию вашего Space (если незнаете что это такое то ничего не выбирайте)
Select the Space SDK - Выбираете Docker
Choose a Docker template - Выбираете Blank
``` 
Дальше ничего не трогаем, листаем в самый низ, проверяем что выбрали именно Public, и нажимаете Create Space

#### Загрузка файлов

Дальше нажимаете на вкладку Files, снизу прикрепил скриншот где именно:

<img width="1918" height="973" alt="image" src="https://github.com/user-attachments/assets/0f344f77-c171-48a7-ab1f-072b8b93d782" />

Вы появитесь на такой странице, где нужно нажать + Contribute: 

<<img width="1918" height="908" alt="image" src="https://github.com/user-attachments/assets/1212a1c5-b119-44c7-8d4d-960d21f411f2" />

там нужно будет выбрать Upload Files: 

<img width="213" height="197" alt="image" src="https://github.com/user-attachments/assets/0579e521-a4a4-415d-ac48-e4ac55484bb9" />

И загружайте туда все файлы с моего репозитория: https://github.com/x4yr/X4yrAC-Tutorial (вы должны скачать зип архив
и разархивировать его например на рабочий стол, и потом все файлы которые вы разархивировали вы должны перенести на
hugging face), после того как вы загрузили мои файлы вы должны еще загрузить **2 файла модели** которые вы сделали в предыдущей главе.

#### Запуск Space

если вы загрузили все файлы то у вас будет вот такой статус Building

! Файлы которые загруженны у меня и у вас будут немного отличаться так что не обращайте внимание !


<img width="1918" height="896" alt="image" src="https://github.com/user-attachments/assets/6d1928c0-1f45-4112-8fb7-e284a15fc88a" />

в течение нескольких секунд он поменяеться на Starting,
и в конце будет Running если вы все сделали правильно:

<img width="1913" height="910" alt="image" src="https://github.com/user-attachments/assets/5f00a1cd-f3d5-4303-8801-6e7a49c19700" />

Если у вас Running то могу вас поздравить, ваша модель античита успешно загружена и была запущена с помощью app.py

#### Подключение вашей модели к серверу

Вы уже сделали большую часть роботы, теперь вам открыть конфиг X4yrAC:

У вас будет такой конфиг:
```yaml
# X4yrAC by _X4YR_
# Лицензия GPL 3.0
# Спасибо за использование античита!

# Полные логи
debug: false

# Директория для сохранение датасетов
outputDirectory: "data"

# Настройки обнаружение
detection:
  # Включить или выключить ИИ модель
  enabled: true
  
  # Тип сервера: "signalr" или "huggingface"
  server-type: "huggingface"
  
  # Для Signalr: полный URL-адрес сервера
  # Для huggingface: https://юзер-спейс.hf.space
  endpoint: "https://юзер-спейс.hf.space/predict"
  
  # API key: обязательно для signalr и huggingface если используете приватный репозиторий
  api-key: ""
  
  # Количество тиков для сбора данных в одной выборке
  sample-size: 40
  
  # Интервал между выборке (в тиках)
  sample-interval: 10
  
  # Интеграция с WorldGuard для игнорирования проверок в определенных регионах.
  # Система приоритетов: если игрок находится в нескольких регионах одновременно,
  # регион с наивысшим приоритетом (priority) определяет, будет ли отключена проверка.
  #
  # Пример: 
  # Игрок находится в "spawn" (приоритет 0) и в "pvp" (приоритет 10).
  # Если в "spawn" проверка отключена, а в "pvp" разрешена — проверка БУДЕТ запущена,
  # так как приоритет региона "pvp" выше.
  worldguard:
    # Включить интграцию с WorldGuard?
    enabled: true
    # Массив регионов где AI проверки выключены.
    # Формат: "world:region" или просто "region"
    disabled-regions:
      - "spawn:spawn"

# Система оповещений
alerts:
  # Вероятность порога для отправки оповещение
  threshold: 0.75
  
  # Отправлять оповещение в консоль?
  console: false

# Система VL
violation:
  # Порог VL (Violation Level) для срабатывания наказания (1 = мгновенное наказание при первом обнаружении)
  threshold: 40
  
  # Значение VL после применения наказания (0 = полный сброс)
  reset-value: 20  
  # Множитель увеличения буфера: (вероятность - порог) * множитель
  # Пример: вероятность 1.0 при пороге 0.70 = (1.0 - 0.70) * 33.33 = 10 к буферу
  multiplier: 100.0
  
  # Снижение буфера, когда вероятность < 0.1 (порог низкой вероятности)
  decay: 0.35
  # Настройки снижения (очистки) VL (сбрасывает VL, когда игрок не в бою)
  # Защищает честных игроков от случайных ложных срабатываний
  vl-decay:
    # Включить автоматическое снижение уровня нарушений (VL)
    enabled: true
    # Интервал проверки в секундах (как часто проверять и снижать VL)
    interval: 200
    # Количество VL, которое снимается за один интервал
    amount: 1

# Настройки наказаний:
# Доступные префиксы действий:
#   {BAN}          - Выполнить команду бана
#   {KICK}         - Выполнить команду кика  
#   {CUSTOM_ALERT} - Отправить кастомное уведомление модераторам
#   (без префикса) - Выполнить «сырую» консольную команду
#
# Доступные плейсхолдеры:
#   {PLAYER}      - Имя игрока
#   {VL}          - Уровень нарушения (Violation level)
#   {PROBABILITY} - Вероятность обнаружения
#   {BUFFER}      - Значение буфера
#
# Также поддерживаются устаревшие плейсхолдеры: <player>, <vl>, <probability>, <buffer>

penalties:
  # Минимальная вероятность, необходимая для применения наказания (снижена для агрессивности)
  min-probability: 0.50
  
  # Префикс для кастомных уведомлений
  custom-alert-prefix: "&7[&#FF0000X4yrAC&7] &f"
  
  # Настройки анимации бана
  animation:
    enabled: true
    # Длительность в тиках (20 тиков = 1 секунда) — уменьшено для быстрого бана
    duration: 80
  
  # Действия в зависимости от уровня нарушений (VL)
  # Агрессивная система: уведомление -> кик -> бан
  actions:
    1: "{CUSTOM_ALERT} &fИгрок &#FF0000{PLAYER} &fподозревается в &#FF0000KillAura&f, с вероятностью &#FF0000{PROBABILITY}&f, Буффер: &#FF0000{BUFFER} &7[&#FF0000VL: {VL}&7]"
    #5: "{BAN} ban {PLAYER} Выебанны by X4yrAC"

# Настройки сообщений:
# Цветовые коды:
#   &0-&f - Стандартные цвета Minecraft
#   &#RRGGBB - Hex-цвета (для версий 1.16+)
#
# Форматирование:
#   &l - Жирный, &o - Курсив, &n - Подчеркнутый, &m - Зачеркнутый, &r - Сброс
messages:
  prefix: "&7[&#FF0000X4yrAC&7] &f"
  
  alerts-enabled: "&fОповещение включены"
  alerts-disabled: "&fОповещение выключены"
  alert-format: "&fИгрок &#FF0000{PLAYER} &fподозревается в &#FF0000KillAura&f, с вероятностью &#FF0000{PROBABILITY}&f, Буффер: &#FF0000{BUFFER}"
  alert-format-vl: "&fИгрок &#FF0000{PLAYER} &fподозревается в &#FF0000KillAura&f, с вероятностью &#FF0000{PROBABILITY}&f, Буффер: &#FF0000{BUFFER} &7[&#FF0000VL: {VL}&7]"
  
  unknown-command: "&fНеизвестные комманды: &#FF0000{ARGS}"
  players-only: "&fЭта комманда для игроков!"
  no-permission: "&fУ вас нету прав!"
  player-not-found: "&fИгрок &#FF0000{PLAYER} &fне найден"
  config-reloaded: "&fКонфигурация перезагружена!"
  
  tracking-stopped: "&fТрекинг включен"
  tracking-started: "&fТрекинг включен для игрока &#FF0000{PLAYER}"
  prob-usage: "&fИспользование: &#FF0000/xac prob <player>"
  player-offline: "&fИгрок оффлайн"
  
  actionbar-format: "Игрок &#FF0000{PLAYER} &7| &fВероятность: &#FF0000{PROBABILITY} &7| &fБуффер: &#FF0000{BUFFER} &7| &fVL: &#FF0000{VL}"
  
  data-status-header: "&7=== Дата-Сеты Статус ==="
  active-sessions: "&fАктивные сессии: &#FF0000{COUNT}"
  no-active-sessions: "&fНету активных сессий"
  start-hint: "&fИспользуйте &#FF0000/xac start <NICK|GLOBAL> <CHEAT|LEGIT|UNLABELED> &fдля начала"
  session-started: "&fНачато &#FF0000{LABEL} &fсессия для &#FF0000{COUNT} игроков"
  session-stopped: "&fОстановленная сессия для игрока &#FF0000{PLAYER}"
  all-sessions-stopped: "&fОстановленно &#FF0000{COUNT} &fсессий"
  no-sessions-to-stop: "&fНету активных сессий для остановки"
  no-players-online: "&fНету игроков онлайн"
  invalid-label: "&fНеправильная метка: &#FF0000{LABEL}"
  valid-labels: "&fВозможные метки: &#FF0000CHEAT, LEGIT, UNLABELED"
  
  usage-header: "&fИспользование:"
  usage-start: "&7  /xac start <NICK|GLOBAL> <CHEAT|LEGIT|UNLABELED> \"<comment>\""
  usage-stop: "&7  /xac stop <NICK|GLOBAL>"
  usage-alerts: "&7  /xac alerts - Включить / Выключить оповещений"
  usage-prob: "&7  /xac prob <игрок> - Смотреть за вероятостью (actionbar)"
  usage-datastatus: "&7  /xac datastatus - Статус сборов датасетов"
  usage-reload: "&7  /xac reload - Перезагрузить конфиг"

# Настройки Folia:
# Эти настройки используются только при работе на сервере Folia.
# На серверах Bukkit/Paper/Spigot эти параметры игнорируются.
folia:
  # Включить оптимизации специально для Folia
  # Если отключено, плагин будет использовать стандартный планировщик Bukkit даже на Folia
  enabled: true
  
  # Размер пула потоков для асинхронных задач (0 = использовать значение Folia по умолчанию)
  thread-pool-size: 0
  
  # Настройки планировщика сущностей (Entity Scheduler)
  # Использовать специфичные для сущностей планировщики для задач, связанных с игроками/мобами
  entity-scheduler:
    enabled: true
  
  # Настройки регионального планировщика (Region Scheduler)
  # Использовать планировщики конкретных регионов для задач, привязанных к местоположению
  region-scheduler:
    enabled: true
```
В нем есть параметр endpoint, туда нужно вставить ссылку к вашей модели, чтобы найти ссылку к вашей модели, вы должны зайти на Hugging Face и вы увидите сверху ссылку на ваш проект, в моем случае она такая, но если вы вставите эту ссылку то у вас работать ничего не будет, вам нужно ее немного изменить.

<img width="1917" height="965" alt="image" src="https://github.com/user-attachments/assets/14e4d1aa-abf9-438b-9377-1aba6896a758" />

В моем случае вот моя ссылка:

https://huggingface.co/spaces/xsdfghj/tutorial/tree/main

из нее я должен взять то что после /spaces/ а именно в моем случае это xsdfghj и tutorial, а /tree/main нам не нужен
первое это ваш user а второе название вашего space, и теперь делаете ссылку в таком формате:

https://user-space.hf.space/

вот что у меня вышло:

https://xsdfghj-tutorial.hf.space/

и это ваша ссылка которую вы должны вставить в конфиг, чтобы получилось вот так:
```yaml
# X4yrAC by _X4YR_
# Лицензия GPL 3.0
# Спасибо за использование античита!

# Полные логи
debug: false

# Директория для сохранение датасетов
outputDirectory: "data"

# Настройки обнаружение
detection:
  # Включить или выключить ИИ модель
  enabled: true
  
  # Тип сервера: "signalr" или "huggingface"
  server-type: "huggingface"
  
  # Для Signalr: полный URL-адрес сервера
  # Для huggingface: https://юзер-спейс.hf.space
  endpoint: "https://xsdfghj-tutorial.hf.space/"
  
  # API key: обязательно для signalr и huggingface если используете приватный репозиторий
  api-key: ""
  
  # Количество тиков для сбора данных в одной выборке
  sample-size: 40
  
  # Интервал между выборке (в тиках)
  sample-interval: 10
  
  # Интеграция с WorldGuard для игнорирования проверок в определенных регионах.
  # Система приоритетов: если игрок находится в нескольких регионах одновременно,
  # регион с наивысшим приоритетом (priority) определяет, будет ли отключена проверка.
  #
  # Пример: 
  # Игрок находится в "spawn" (приоритет 0) и в "pvp" (приоритет 10).
  # Если в "spawn" проверка отключена, а в "pvp" разрешена — проверка БУДЕТ запущена,
  # так как приоритет региона "pvp" выше.
  worldguard:
    # Включить интграцию с WorldGuard?
    enabled: true
    # Массив регионов где AI проверки выключены.
    # Формат: "world:region" или просто "region"
    disabled-regions:
      - "spawn:spawn"

# Система оповещений
alerts:
  # Вероятность порога для отправки оповещение
  threshold: 0.75
  
  # Отправлять оповещение в консоль?
  console: false

# Система VL
violation:
  # Порог VL (Violation Level) для срабатывания наказания (1 = мгновенное наказание при первом обнаружении)
  threshold: 40
  
  # Значение VL после применения наказания (0 = полный сброс)
  reset-value: 20  
  # Множитель увеличения буфера: (вероятность - порог) * множитель
  # Пример: вероятность 1.0 при пороге 0.70 = (1.0 - 0.70) * 33.33 = 10 к буферу
  multiplier: 100.0
  
  # Снижение буфера, когда вероятность < 0.1 (порог низкой вероятности)
  decay: 0.35
  # Настройки снижения (очистки) VL (сбрасывает VL, когда игрок не в бою)
  # Защищает честных игроков от случайных ложных срабатываний
  vl-decay:
    # Включить автоматическое снижение уровня нарушений (VL)
    enabled: true
    # Интервал проверки в секундах (как часто проверять и снижать VL)
    interval: 200
    # Количество VL, которое снимается за один интервал
    amount: 1

# Настройки наказаний:
# Доступные префиксы действий:
#   {BAN}          - Выполнить команду бана
#   {KICK}         - Выполнить команду кика  
#   {CUSTOM_ALERT} - Отправить кастомное уведомление модераторам
#   (без префикса) - Выполнить «сырую» консольную команду
#
# Доступные плейсхолдеры:
#   {PLAYER}      - Имя игрока
#   {VL}          - Уровень нарушения (Violation level)
#   {PROBABILITY} - Вероятность обнаружения
#   {BUFFER}      - Значение буфера
#
# Также поддерживаются устаревшие плейсхолдеры: <player>, <vl>, <probability>, <buffer>

penalties:
  # Минимальная вероятность, необходимая для применения наказания (снижена для агрессивности)
  min-probability: 0.50
  
  # Префикс для кастомных уведомлений
  custom-alert-prefix: "&7[&#FF0000X4yrAC&7] &f"
  
  # Настройки анимации бана
  animation:
    enabled: true
    # Длительность в тиках (20 тиков = 1 секунда) — уменьшено для быстрого бана
    duration: 80
  
  # Действия в зависимости от уровня нарушений (VL)
  # Агрессивная система: уведомление -> кик -> бан
  actions:
    1: "{CUSTOM_ALERT} &fИгрок &#FF0000{PLAYER} &fподозревается в &#FF0000KillAura&f, с вероятностью &#FF0000{PROBABILITY}&f, Буффер: &#FF0000{BUFFER} &7[&#FF0000VL: {VL}&7]"
    #5: "{BAN} ban {PLAYER} Выебанны by X4yrAC"

# Настройки сообщений:
# Цветовые коды:
#   &0-&f - Стандартные цвета Minecraft
#   &#RRGGBB - Hex-цвета (для версий 1.16+)
#
# Форматирование:
#   &l - Жирный, &o - Курсив, &n - Подчеркнутый, &m - Зачеркнутый, &r - Сброс
messages:
  prefix: "&7[&#FF0000X4yrAC&7] &f"
  
  alerts-enabled: "&fОповещение включены"
  alerts-disabled: "&fОповещение выключены"
  alert-format: "&fИгрок &#FF0000{PLAYER} &fподозревается в &#FF0000KillAura&f, с вероятностью &#FF0000{PROBABILITY}&f, Буффер: &#FF0000{BUFFER}"
  alert-format-vl: "&fИгрок &#FF0000{PLAYER} &fподозревается в &#FF0000KillAura&f, с вероятностью &#FF0000{PROBABILITY}&f, Буффер: &#FF0000{BUFFER} &7[&#FF0000VL: {VL}&7]"
  
  unknown-command: "&fНеизвестные комманды: &#FF0000{ARGS}"
  players-only: "&fЭта комманда для игроков!"
  no-permission: "&fУ вас нету прав!"
  player-not-found: "&fИгрок &#FF0000{PLAYER} &fне найден"
  config-reloaded: "&fКонфигурация перезагружена!"
  
  tracking-stopped: "&fТрекинг включен"
  tracking-started: "&fТрекинг включен для игрока &#FF0000{PLAYER}"
  prob-usage: "&fИспользование: &#FF0000/xac prob <player>"
  player-offline: "&fИгрок оффлайн"
  
  actionbar-format: "Игрок &#FF0000{PLAYER} &7| &fВероятность: &#FF0000{PROBABILITY} &7| &fБуффер: &#FF0000{BUFFER} &7| &fVL: &#FF0000{VL}"
  
  data-status-header: "&7=== Дата-Сеты Статус ==="
  active-sessions: "&fАктивные сессии: &#FF0000{COUNT}"
  no-active-sessions: "&fНету активных сессий"
  start-hint: "&fИспользуйте &#FF0000/xac start <NICK|GLOBAL> <CHEAT|LEGIT|UNLABELED> &fдля начала"
  session-started: "&fНачато &#FF0000{LABEL} &fсессия для &#FF0000{COUNT} игроков"
  session-stopped: "&fОстановленная сессия для игрока &#FF0000{PLAYER}"
  all-sessions-stopped: "&fОстановленно &#FF0000{COUNT} &fсессий"
  no-sessions-to-stop: "&fНету активных сессий для остановки"
  no-players-online: "&fНету игроков онлайн"
  invalid-label: "&fНеправильная метка: &#FF0000{LABEL}"
  valid-labels: "&fВозможные метки: &#FF0000CHEAT, LEGIT, UNLABELED"
  
  usage-header: "&fИспользование:"
  usage-start: "&7  /xac start <NICK|GLOBAL> <CHEAT|LEGIT|UNLABELED> \"<comment>\""
  usage-stop: "&7  /xac stop <NICK|GLOBAL>"
  usage-alerts: "&7  /xac alerts - Включить / Выключить оповещений"
  usage-prob: "&7  /xac prob <игрок> - Смотреть за вероятостью (actionbar)"
  usage-datastatus: "&7  /xac datastatus - Статус сборов датасетов"
  usage-reload: "&7  /xac reload - Перезагрузить конфиг"

# Настройки Folia:
# Эти настройки используются только при работе на сервере Folia.
# На серверах Bukkit/Paper/Spigot эти параметры игнорируются.
folia:
  # Включить оптимизации специально для Folia
  # Если отключено, плагин будет использовать стандартный планировщик Bukkit даже на Folia
  enabled: true
  
  # Размер пула потоков для асинхронных задач (0 = использовать значение Folia по умолчанию)
  thread-pool-size: 0
  
  # Настройки планировщика сущностей (Entity Scheduler)
  # Использовать специфичные для сущностей планировщики для задач, связанных с игроками/мобами
  entity-scheduler:
    enabled: true
  
  # Настройки регионального планировщика (Region Scheduler)
  # Использовать планировщики конкретных регионов для задач, привязанных к местоположению
  region-scheduler:
    enabled: true
```

#### Документация по конфигу

Документацию я решил сделать прямо внутри конфига, в виде коментариев, если что то будет непонятно то пишите мне в дискорд или телеграмм:
```
тг: @x4yr_tg
дс: xayrllano_77932
```

Спасибо за использование античита!

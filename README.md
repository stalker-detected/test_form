### Як запустити автотести?
Для запуска автотестів потрібен Maven на ПК.
1) З консолі перейти в папку з автотестами
2) Запустити команду mvn test -Dsurefire.suiteXmlFiles=testng.xml

### Що роблять автотести?
Передумова перед запуском кожного теста: перехід на https://docs.google.com/forms/d/e/1FAIpQLSdqT5F9_qhPDmJ4lfIH7buVkUvjf4LS9ODdqD7PYfVbfFTnpA/viewform 

1) Тест нічого не вводить, а  тисне "Отправить". Перевірка що в першому блоці появляється повідомлення "Это обязательный вопрос.".
2) Перевірка що якщо ввести всі дані і відправити форму, то переходить на сторінку на якій написано що відповідь записана. Буду називати її "фініш".
3) Перевірити заголовок кожного блока.  Наприклад що Your name: = Your name:.
4) Тест заповнює інформацію у всі поля, потім очищає. Перевірка що в кожному блоці появляється повідомлення "Это обязательный вопрос.".
5) Тест вводить через DataProvider не валідні емейли. Перевірка що в такому разі виводиться повідомлення "Укажите действительный адрес эл. почты".
6) Тест вводить через DataProvider валідні емейли. Перевірка що в такому разі не виводиться повідомлення "Укажите действительный адрес эл. почты".
7) Перевірка що всі поля пусті по замовчуванню.
8) Тест перевіряє що якщо ввести дату народження більшу за сьогодні, то появляється текст помилки.
9) Перевірка що якщо в чекбоксах натиснути "Другое",  то потрібно обов'язково вводити комент.  Обов'язково не пустий.
10) Перевірка що в чекбоксах має вибиратись тільки один варіант, тому що настрій(mood) може бути тільки один.
11) Тест переходить на сторінку "фініша", клікає на "Отправить ещё один ответ" і перевіряє що відкрилась знову сторінка з анкетою.
12) Перевірка що при перезавантаженні сторінки пояляється alert.
13) Перевірка що при закритті сторінки пояляється alert.

### Багрепорти

#### Форма дає ввести дату народження яка більша теперішнього числа

**Кроки відтворення:**
1) Перейти на https://docs.google.com/forms/d/e/1FAIpQLSdqT5F9_qhPDmJ4lfIH7buVkUvjf4LS9ODdqD7PYfVbfFTnpA/viewform
2) В поле "Вік" ввести дату 23.11.8000
3) Натисути "Отправить"

**Очікуваний результат:** має відобразитись помилка що введена не вірна дата. Наприклад "Введена дата не може бути більше сьогоднішнього числа".

**Фактичний результат:** ніяких помилок не відображається. 

<hr>

#### Якщо стерти "Ваш вік", то не пишеться що це обов'язкове питання!

**Кроки відтворення:**
1) Перейти на https://docs.google.com/forms/d/e/1FAIpQLSdqT5F9_qhPDmJ4lfIH7buVkUvjf4LS9ODdqD7PYfVbfFTnpA/viewform
2) В поле "Вік" ввести будь-яку дату і вибрати її
3) Ще раз зайти в календар і видалити дату кнопкою "Удалить"

**Очікуваний результат:** має відобразитись помилка "Это обязательный вопрос."

**Фактичний результат:** ніяких помилок не відображається. 

<hr>

#### Поле вводу емейлів приймає некоректні адреси електронної пошти

**Кроки відтворення:**
1) Перейти на https://docs.google.com/forms/d/e/1FAIpQLSdqT5F9_qhPDmJ4lfIH7buVkUvjf4LS9ODdqD7PYfVbfFTnpA/viewform
2) Ввести некоректний емейл зі списку
3) Натиснути "Отправить"

Некоректні емейли:<br/>
Joe Smith <email@domain.com><br/>
.email@domain.com<br/>
email@111.222.333.44444<br/>
.mysite@mysite.org<br/>
mysite..1234@yahoo.com<br/>

**Очікуваний результат:** має відобразитись помилка "Укажите действительный адрес эл. почты".

**Фактичний результат:** ніяких помилок не відображається, форма відправляється.

<hr>

#### Можна вибрати 2 і більше настроїв

1) Перейти на https://docs.google.com/forms/d/e/1FAIpQLSdqT5F9_qhPDmJ4lfIH7buVkUvjf4LS9ODdqD7PYfVbfFTnpA/viewform
2) В блоці вибору настрою вибрати "Excellent" і "Good enough"

**Очікуваний результат:** при виборі одного чекбокса, інші мають бути неактивними.

**Фактичний результат:** можна вибрати зразу всі настрої і відправити форму.

### Пройшло вже багато часу і тести поламались. Нажаль =(
package client.localization;

import java.util.ListResourceBundle;

public class Locale_ru_RU extends ListResourceBundle {
    private Object[][] contents = {
            {"Enter the X coordinate (X > -934):", "Введите координату X (X > -934):"},
            {"The coordinate must be > -934, please try again:", "Координата должна быть > -934, повторите ввод:"},
            {"Input format - integer number, please try again:", "Формат ввода - целое число, повторите ввод:"},
            {"Input completed by user", "Ввод завершен пользователем"},
            {"Enter the Y coordinate (Y <= 946):", "Введите координату Y (Y <= 946):"},
            {"The coordinate must be <= 946, please try again:", "Координата должна быть <= 946, повторите ввод:"},
            {"Input format - floating-point number, please try again:", "Формат ввода - число с плавающей точкой, повторите ввод:"},
            {"Enter the name field:", "Введите поле name:"},
            {"Name cannot be empty, please try again:", "Имя не может быть пустым, повторите ввод:"},
            {"Enter the minimum point:", "Введите минимальный балл:"},
            {"The point must be > 0, please try again:", "Балл должен быть > 0, повторите ввод:"},
            {"Enter the average point:", "Введите средний балл:"},
            {"Enter the difficulty. Possible options:", "Введите сложность. Возможные варианты:"},
            {"Value must be specified, please try again:", "Значение должно быть указано, повторите ввод:"},
            {"No such option, please try again:", "Такого варианта нет, повторите ввод:"},
            {"Enter the discipline name (empty if discipline is not filled):", "Введите имя дисциплины (пустое имя, если дисциплина не заполняется):"},
            {"Enter the number of lecture hours:", "Введите кол-во часов лекций:"},
            {"Enter the number of practical hours:", "Введите кол-во часов практики:"},
            {"Enter the number of self-study hours:", "Введите кол-во часов самостоятельного изучения:"},
            {"Exiting the program...", "Выход из программы..."},
            {"Usage: exit", "Использование: exit"},
            {"Usage: execute_script {file_name}", "Использование: execute_script {file_name}"},
            {"Usage: show", "Использование: show"},
            {"The collection is empty", "Коллекция пуста"},
            {"Input error in script", "Ошибка ввода команд в скрипте"},
            {"Command ", "Команды "},
            {" does not exist, enter 'help' for help", " не существует, введите help для справки"},
            {"Executing script ", "Выполнение скрипта "},
            {"...", "..."},
            {"Script ", "Скрипт "},
            {" executed successfully!", " успешно выполнен!"},
            {"File not found", "Файл не найден"},
            {"No permission to read the file", "Нет прав на чтение файла"},
            {"The script file is empty", "Файл скрипта пуст"},
            {"Recursive call detected in the script", "В скрипте обнаружена рекурсия"},
            {"We apologize, the server has moved the horses", "Приносим искренние извинения, сервер двинул кони"},
            {"The username is too short", "Имя пользователя слишком короткое"},
            {"The password is too short", "Пароль слишком короткий"},
            {"Enter the username (minimum 3 characters):", "Введите имя пользователя (минимум 3 символа):"},
            {"Enter the username:", "Введите имя пользователя:"},
            {"Enter the password (minimum 6 characters):", "Введите пароль (минимум 6 символов):"},
            {"Enter the password:", "Введите пароль:"},
            {"Password encryption error", "Ошибка шифрования пароля"},
            {"Lab repository", "Лабохранилище"},
            {"Log in", "Войти"},
            {"Register", "Зарегистрироваться"},
            {"Username", "Имя пользователя"},
            {"Password", "Пароль"},
            {"Select an action...", "Выберите действие..."},
            {"Filter", "Фильтр"},
            {"Sorting", "Сортировка"},
            {"Visualization", "Визуализация"},
            {"Name", "Название"},
            {"Creation Date", "Дата создания"},
            {"Minimum point", "Минимальный балл"},
            {"Average point", "Средний балл"},
            {"Difficulty", "Сложность"},
            {"Discipline Name", "Название дисциплины"},
            {"Lecture Hours", "Кол-во часов лекций"},
            {"Practice Hours", "Кол-во часов практики"},
            {"Self-study Hours", "Кол-во часов самостоятельного изучения"},
            {"Owner", "Владелец"},
            {"Add", "Добавить"},
            {"Add if Min", "Добавить, если наименьший"},
            {"Update by ID", "Обновить по ID"},
            {"Remove", "Удалить"},
            {"Remove by ID", "Удалить по ID"},
            {"Remove Greater", "Удалить больший"},
            {"Clear", "Очистить"},
            {"Average Minimum point", "Средний минимальный балл"},
            {"Minimum by ID", "Минимальный по ID"},
            {"Minimum point Ascending", "Минимальный балл по возрастанию"},
            {"Execute Script", "Выполнить скрипт"},
            {"Information", "Информация"},
            {"The collection is empty", "Коллекция пуста"},
            {"Error connecting to the server", "Ошибка установления соединения с сервером"},
            {"Object addition", "Добавление объекта"},
            {"Object editing", "Редактирование объекта"},
            {"Object removal", "Удаление объекта"},
            {"This user is already registered", "Такой пользователь уже зарегистрирован"},
            {"The server is temporarily unavailable. Please try again later.", "Сервер временно недоступен. Попробуйте позже."},
            {"Invalid username or password", "Неверное имя пользователя или пароль"},
            {"User:", "Пользователь:"},
            {"No element in the collection with this ID", "В коллекции нет элемента с таким ID"},
            {"No element selected for removal", "Не выбран элемент для удаления"},
            {"Are you sure you want to clear your collection?", "Вы уверены, что хотите очистить свою коллекцию?"},
            {"Object with the minimum ID", "Объект с минимальным ID"},
            {"Minimum point in ascending order:", "Минимальный балл в порядке возрастания:"},
            {"Collection Editor", "Редактор коллекции"},
            {"Enter ID", "Введите ID"},
            {"Sorting and Filtering", "Сортировка и фильтрация"},
            {"Sorting", "Сортировка"},
            {"Filtering", "Фильтр"},
            {"Name (not empty):", "Имя (не пустое):"},
            {"X Coordinate (greater than -934):", "Координата X (больше -934):"},
            {"Y Coordinate (not greater than 946):", "Координата Y (не больше 946):"},
            {"Minimum point (greater than 0):", "Минимальный балл (больше 0):"},
            {"Average point (greater than 0):", "Средний балл (больше 0):"},
            {"Difficulty:", "Сложность:"},
            {"Discipline Name (empty if not filled):", "Название дисциплины (пустое, если дисциплина не заполняется):"},
            {"Lecture Hours:", "Количество часов лекций:"},
            {"Practice Hours:", "Количество часов практики:"},
            {"Self-study Hours:", "Количество часов самостоятельного изучения:"},
            {"Save", "Сохранить"},
            {"Cancel", "Отмена"},
            {"Invalid values entered", "Введены недопустимые значения"},
            {"Not all required fields are filled", "Заполнены не все обязательные поля"},
            {"The server is unavailable, please try again later", "Сервер недоступен, попробуйте позже"},
            {"Ascending", "По возрастанию"},
            {"Descending", "По убыванию"},
            {"Enter a value for the filter", "Введите значение для фильтра"},
            {"Select a field for sorting", "Выберите поле для сортировки"},
            {"Reset", "Сбросить"},
            {"Ok", "Ok"},
            {"Open script file", "Открыть файл скрипта"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}

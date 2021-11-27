import time
import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains


# Так как мы часто логинимся создадим метод
def login(name, password, driver):
    driver.get('https://ruswizard.su/test/wp-login.php')
    for i in range(20):
        driver.find_element(By.ID, "user_login").send_keys(Keys.BACK_SPACE)
    WebDriverWait(driver, 100).until(EC.visibility_of_element_located((By.ID, 'user_login'))) \
        .send_keys(name)
    driver.find_element(By.ID, 'user_pass').send_keys(password)
    driver.find_element(By.ID, 'wp-submit').click()
    WebDriverWait(driver, 100).until(EC.visibility_of_element_located((By.CSS_SELECTOR, 'h1:nth-child(1)')))


def change_post_privacy(driver):
    driver.get("https://ruswizard.su/test/wp-admin/edit.php")
    WebDriverWait(driver, 100).until(EC.visibility_of_element_located((By.CSS_SELECTOR, ".wp-heading-inline")))

    post = driver.find_element(By.CLASS_NAME, "title.column-title.has-row-actions.column-primary.page-title")
    ActionChains(driver).move_to_element(post).perform()

    driver.find_element(By.CLASS_NAME, "button-link.editinline").click()
    driver.find_element(By.NAME, "keep_private").click()
    driver.find_element(By.CLASS_NAME, "button.button-primary.save.alignright").click()


def create_post(driver, text):
    driver.get(url='https://ruswizard.su/test/wp-admin/post-new.php')
    post = WebDriverWait(driver, 60).until(
        EC.presence_of_element_located((By.ID, 'post-title-0'))
    )

    post.send_keys(text)
    driver.find_element(By.XPATH, "//div[@id='editor']/div/div/div/div/div[2]/button[2]").click()
    WebDriverWait(driver, 100).until(
        EC.visibility_of_element_located((
            By.XPATH, "//div[@id='editor']/div/div/div[2]/div[4]/div[2]/div/div/div/div/button")))
    driver.find_element(By.XPATH, "//div[@id='editor']/div/div/div[2]/div[4]/div[2]/div/div/div/div/button").click()
    WebDriverWait(driver, 100).until(
        EC.visibility_of_element_located((
            By.CLASS_NAME, "post-publish-panel__postpublish")))

    return driver.find_element(By.ID, 'inspector-text-control-0').get_attribute("value")


def create_post_on_hold(driver, text):
    # create new post
    driver.get(url='https://ruswizard.su/test/wp-admin/post-new.php')
    post = WebDriverWait(driver, 100).until(
        EC.presence_of_element_located((By.ID, 'post-title-0'))
    )
    post.send_keys(text)

    driver.find_element(By.CLASS_NAME, "components-button.edit-post-post-schedule__toggle.is-tertiary").click()
    WebDriverWait(driver, 100).until(
        EC.visibility_of_element_located((By.CLASS_NAME, "components-datetime__time-field-hours-input")))
    h_field = driver.find_element(By.CLASS_NAME, "components-datetime__time-field-hours-input")
    m_field = driver.find_element(By.CLASS_NAME, "components-datetime__time-field-minutes-input")

    hours = int(h_field.get_attribute("value"))
    minutes = int(m_field.get_attribute("value"))

    #hours = int(h_field.get_attribute("value")) + 1
    if minutes + 2 > 59:
        hours += 1
        minutes = minutes + 1 - 59
    else:
        minutes += 2

    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-hours-input").click()
    time.sleep(0.5)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-hours-input").send_keys(Keys.ARROW_RIGHT)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-hours-input").send_keys(Keys.BACK_SPACE)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-hours-input").send_keys(Keys.BACK_SPACE)
    time.sleep(0.5)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-hours-input").send_keys(str(hours))
    time.sleep(0.5)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-minutes-input").click()
    time.sleep(0.5)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-minutes-input").send_keys(Keys.ARROW_RIGHT)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-minutes-input").send_keys(Keys.BACK_SPACE)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-minutes-input").send_keys(Keys.BACK_SPACE)
    time.sleep(0.5)
    driver.find_element(By.CLASS_NAME, "components-datetime__time-field-minutes-input").send_keys(str(minutes))
    time.sleep(0.5)

    # чтобы поле ввода закрылось и значения сохранились
    driver.find_element(By.CLASS_NAME, "components-button.components-panel__body-toggle").click()

    driver.find_element(By.XPATH,
                        "//div[@id='editor']/div/div/div/div/div[2]/button[2]").click()
    driver.find_element(By.XPATH,
                        "//div[@id='editor']/div/div/div[2]/div[4]/div[2]/div/div/div/div/button").click()

    WebDriverWait(driver, 100).until(
        EC.visibility_of_element_located((
            By.CLASS_NAME, "post-publish-panel__postpublish")))

    return driver.find_element(By.ID, 'inspector-text-control-0').get_attribute("value")


def delete_post(driver, text, additional_click=False):
    driver.get("https://ruswizard.su/test/wp-admin/edit.php")
    WebDriverWait(driver, 100).until(EC.visibility_of_element_located((By.CSS_SELECTOR, ".wp-heading-inline")))
    driver.find_element(By.LINK_TEXT, text).click()
    if additional_click:
        WebDriverWait(driver, 100).until(
            EC.visibility_of_element_located(
                (By.CLASS_NAME, "components-button.components-panel__body-toggle")))
        driver.find_element(By.CLASS_NAME, "components-button.components-panel__body-toggle").click()
    WebDriverWait(driver, 100).until(
        EC.visibility_of_element_located(
            (By.CLASS_NAME, "components-button.editor-post-trash.is-tertiary.is-destructive")))
    driver.find_element(By.CLASS_NAME, "components-button.editor-post-trash.is-tertiary.is-destructive").click()
    WebDriverWait(driver, 100).until(EC.visibility_of_element_located((By.CSS_SELECTOR, ".wp-heading-inline")))


def add_comment(driver, link, bad=False, logged=False):
    text = "geek" if bad else "sosi nogi"
    driver.get(link)
    driver.find_element(By.ID, "comment").send_keys(text)

    if not logged:
        author = "KenKaneki"
        driver.find_element(By.ID, "author").send_keys(author)
        driver.find_element(By.ID, "email").send_keys(author + "@yandex.ru")

    driver.find_element(By.ID, "submit").send_keys(Keys.ENTER)
    return text


def check_page_text(driver, link, text):
    driver.get(link)
    WebDriverWait(driver, 100).until(EC.visibility_of_element_located((By.CLASS_NAME, "entry-title")))
    assert driver.find_element(By.CLASS_NAME, "entry-title").text == text


class WebTest(unittest.TestCase):
    def setUp(self):
        self.driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")

    # Сценарий:
    # Пользователь создает запись, происходит попытка найти запись
    # в списке всех записей, при успехе запись удаляется.
    # После удаления происходит проверка, что запись больше недоступна
    def test_create_and_delete_record(self):
        driver = self.driver
        driver.implicitly_wait(100)

        # Логинимся и создаем запись
        login('dead_inside', '123', driver)
        text = "986 - 7"
        link = create_post(driver, text)

        # Удаляем запись и проверяем что она больше недоступна
        delete_post(driver, "986 — 7")
        driver.get(link)
        assert "Страница не найдена" == driver.find_element(By.CSS_SELECTOR, ".entry-title").text

    # Сценарий:
    # Пользователь создает запись, ставит задержку в один час
    # происходит попытка просмотреть запись без аккаунта, записи быть не должно.
    # Далее с аккаунта, создавшего запись, происходит вход на страницу со списком всех его записей,
    # проверяется, что в этом списке у записи появился соответствующий статус - "Запланировано"
    # После всех проверок запись удаляется
    def test_create_post_on_hold(self):
        driver = self.driver
        text = "Задержка в развитии"

        login('dead_inside', '123', driver)
        link = create_post_on_hold(driver, text)

        second_driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")
        check_page_text(second_driver, link, "Страница не найдена")
        second_driver.close()

        driver.get("https://ruswizard.su/test/wp-admin/edit.php")
        assert driver.find_element(By.CLASS_NAME, "post-state").text == "Запланировано"

        time.sleep(120)
        second_driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")
        check_page_text(second_driver, link, "Задержка в развитии")
        second_driver.close()

        delete_post(driver, "Задержка в развитии", True)

    # Сценарий:
    # Пользователь создает запись
    # Происходит попытка просмотреть запись с другого аккаунта, запись должна быть видима.
    # Происходит попытка просмотреть запись без аккаунта аккаунта, запись должна быть видима.
    # После прохождения всех проверок пользователь удаляет запись
    def test_record_from_another_account(self):
        driver = self.driver
        driver.implicitly_wait(100)

        login('dead_inside', '123', driver)
        text = "1000 - 7"
        link = create_post(driver, text)

        second_driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")
        login('BigDickIsBackInTown', '12345678', second_driver)
        # Проверяем запись с чужого аккаунта
        check_page_text(second_driver, link, "1000 — 7")
        second_driver.close()

        # Проверяем без аккаунта
        third_driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")
        check_page_text(third_driver, link, "1000 — 7")
        third_driver.close()

        delete_post(driver, "1000 — 7")

    # Сценарий:
    # Пользователь создает запись,
    # изменяет настройки приватности, чтобы запись стала личной, и сохраняет настройки.
    # Происходит попытка просмотреть запись с другого аккаунта, записи по ссылке быть не должно.
    # Происходит попытка просмотреть запись без аккаунта аккаунта, записи по ссылке быть не должно.
    # Настройки приватность возвращаются назад и проверяется просмотр той же записи (после рефреша страницы)
    # Происходит попытка просмотреть запись с другого аккаунта, запись должна быть видима.
    # Происходит попытка просмотреть запись без аккаунта аккаунта, запись должна быть видима.
    # После всех проверок запись удаляется
    def test_private_record_from_another_account(self):
        driver = self.driver
        driver.implicitly_wait(100)

        login('dead_inside', '123', driver)
        text = "1000 - 7"
        link = create_post(driver, text)
        change_post_privacy(driver)

        second_driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")
        login('BigDickIsBackInTown', '12345678', second_driver)
        # Проверяем запись с чужого аккаунта
        check_page_text(second_driver, link, "Страница не найдена")

        # Проверяем без аккаунта
        third_driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")
        check_page_text(third_driver, link, "Страница не найдена")

        change_post_privacy(driver)

        # Проверяем запись с чужого аккаунта
        second_driver.refresh()
        check_page_text(second_driver, link, "1000 — 7")
        second_driver.close()

        # Проверяем без аккаунта
        third_driver.refresh()
        check_page_text(third_driver, link, "1000 — 7")
        third_driver.close()
        delete_post(driver, "1000 — 7")

    # Сценарий:
    # Пользователь создает запись и оставляет на ней комментарий с модерируемым словом - "geek",
    # модерации произойти не должно, так как комментарий происходит с аккаунта.
    # Также комментарий должен быть сразу апрувнут, так как его оставляет владелец записи.
    # Проверяется наличие комментария и его текст.
    # После всех проверок запись удаляется
    def test_add_comment_from_logged_user(self):
        driver = self.driver
        text = "I am ghoul"

        login('dead_inside', '123', driver)
        link = create_post(driver, text)
        comment = add_comment(driver, link, True, True)

        # Проверяем, что комментарий сразу апрувнут
        driver.get("https://ruswizard.su/test/wp-admin/edit.php")
        driver.find_element(By.CLASS_NAME, "comment-count-approved").click()

        assert driver.find_element(By.CLASS_NAME,
                                   "comment.column-comment.has-row-actions.column-primary").text == comment
        delete_post(driver, text)

    # Сценарий:
    # Пользователь создает запись
    # Пользователь без аккаунта оставляет на ней 2 комментария - с и без модерируемого слова.
    # Комментарий отправляется на подтверждение, так как пользователь не имеет отношения к записи.
    # Проверяется наличие одного комментария (с модерируемым словом был забанен) и его текст.
    # После всех проверок запись удаляется
    def test_add_comment_from_unlogged_user(self):
        driver = self.driver
        text = "I am ghoul"
        login('dead_inside', '123', driver)
        link = create_post(driver, text)

        second_driver = webdriver.Chrome("/Users/rinokus/Downloads/chromedriver")
        good_comment = add_comment(second_driver, link, False, False)
        time.sleep(15)  # таймаут, а то ругается на два комментария подряд
        _ = add_comment(second_driver, link, True, False)
        second_driver.close()

        driver.get("https://ruswizard.su/test/wp-admin/edit.php")

        # Проверяем, что всего один комментарий отправился (С гиком забанен)
        assert driver.find_element(By.CLASS_NAME, "comment-count-pending").text == '1'
        driver.find_element(By.CLASS_NAME, "post-com-count.post-com-count-pending").click()
        # И проверяем, что там верный текст
        assert driver.find_element(By.CLASS_NAME,
                                   "comment.column-comment.has-row-actions.column-primary").text == good_comment
        delete_post(driver, text)

    def tearDown(self):
        self.driver.close()


if __name__ == "__main__":
    unittest.main()

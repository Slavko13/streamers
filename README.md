<p>Для запуска необходим docker</p> 
<p>Собрать проект - <i>mvn clean install</i></p>
<p>Сделать билд Dockerfile (точка тут не просто так если что)- <i>docker build -t my-app .</i></p>
<p>Выполнить запуск докер образов с помощью файла для тест - <i>docker-compose -f docker-compose.test.yml up -d</i> </p>

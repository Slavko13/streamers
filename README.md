<p>Для запуска необходим docker</p> 
<p>Собрать проект - <i>mvn clean install</i></p>
<p>Сделать билд Dockerfile (точка тут не просто так если что)- <i>docker build -t my-app .</i></p>
<p>Выполнить запуск докер образов с помощью файла для тест - <i>docker-compose -f docker-compose.test.yml up -d</i> </p>

<p>Используемые технологии</p>
<ul>
  <li>Spring boot</li>
  <li>Java 17</li>
  <li>Swagger 2</li>
  <li>Keycloak</li>
  
</ul>
<p>Имеется ветка Auth с готовой реализацией на будущее</p>

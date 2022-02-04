# product-list
repository for product-list project

ссылка на swagger: https://app.swaggerhub.com/apis/AndrewKozyrev/ProductList/1.0#/default/get_products


# Техническое задание
1.	Список продуктов

Сделать сервис с использованием Spring Boot, Postgresql.

Использование MongoDb вместо Postgresql и/или Swagger будет преимуществом.

Сервис должен иметь несколько API endpoint’ов. Формат запроса и ответа - json.

Объекты

## Product

long : id

String : name

String : description

int : kcal

## List 
long : id

String : name


Связи
В каждом List может быть любое количество Product.
## Задача

Сохранение 

Нужно сделать web сервис, который будет сохранять по API: 
-	Product 
-	List 
-	добавлять Product в созданный List  

Количество связей должно быть ограничено пунктом Связи. 

Получение 
API должно давать возможность получать списки: 
-	Product 
-	List со всеми Product, которые относятся к данному List по id 


Усложнение 

Добавьте Swagger к проекту. В запросе List просуммируйте kcal из Product в списке и верните вместе с List в ответе на запрос. 


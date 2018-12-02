The Application: We are developing an application which allows the user to add items to a
“To-buy-list” and get notifications about the store nearby which supplies the item.

Main features of the app: A group of people can be created, who could post shared items. In such
a case, the group member who is close to the store will get a notification about the product and
the near by store which makes it available. The person who buys the item can then update the
amount owed to him by the other group members.

Approach to the features: We will create a remote server which will allow the user to register and
login into the app, add/delete the items to the “To-buy-list”, and notify the user when he is closer
than 50 m to the store. The app will run as a service and query the list from remote database to
come up with the items and find a near by place that offers it and notifies the user. The amount
owed by the user or to the user would be retrieved from the database and relevant updates
would be displayed to particular user.

APIs used: Google Places, Google Maps, Google play services location API.

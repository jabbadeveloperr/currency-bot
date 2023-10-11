Backend Developer Test Task
Task: create a Telegram bot that will notify user about cryptocurrency price changes. User will start at some time, for example at 8 am.
After that, if some cryptocurrency becomes more expensive or cheaper by more than N percent since 8 am, user should be notified with message in Telegram.
URL to check prices for crypto: https://api.mexc.com/api/v3/ticker/price. Algorithm should make a request and refresh application state every S seconds.
User should have ability to restart algorithm. Telegram bot can support K users, K + 1 user should be notified that bot is not available.
Technology to use: Spring Boot. Database is by candidate decision. Task should take 4 hours maximum.
Task can be unfinished if it is taking more time. The point is not to have bot at the end of task but to see how candidate will show himself
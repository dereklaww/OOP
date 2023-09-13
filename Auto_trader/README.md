# Project Overview
Developed a stock exchange application in the Java programming environment which implements the Price-Time Priority Algorithm.
# Price-Time Priority Algorithm
The Price-Time Priority Algorithm first prioritises by price, and then uses time to differentiate between orders at the same price. If a sell order is made, the algorithm will find a corresponding buy order for the same product with the highest price greater or equal to the price specified by the sell order. If there are multiple such buy orders, the earliest one is used.
If a buy order is made, the algorithm will find a corresponding sell order for the same price with the lowest price less than or equal to the price specified by the buy order. If there are multiple such sell orders, the earliest one is used.
# To run
- compile files using javac
- java Exchange

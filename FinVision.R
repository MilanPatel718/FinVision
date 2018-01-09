singlePortfolio <- function(TickerList, Size, start, end, movingAverage){ 
  #Install and Load Packages if needed
  if (!require("rJava")) {
    install.packages("rJava")
    library(quantmod)
  }
  
  if (!require("quantmod")) {
    install.packages("quantmod")
    library(quantmod)
  }
  
  if (!require("TTR")) {
    install.packages("TTR")
    library(TTR)
  }

  #Make sure parameters passed from Java are formatted correctly  
  TickerList <- as.vector(TickerList)
  Size <- as.integer(Size)
  start <- as.Date(start)
  end <- as.Date(end)
  movingAverage <- as.integer(movingAverage)

  tradingDays <- sum(!weekdays(seq(start, end, "days")) %in% c("Saturday", "Sunday"))
  
  #Remove tickers from TickerList that are invalid so that getSymbols() can run without error
  symbolFunction <- function(sym){
    return (tryCatch(getSymbols(sym, src = "yahoo", from = start, to = end), error= function(e) NULL))
  }
  adjustedSymbols <- unlist(lapply(TickerList, symbolFunction))
  
  #Load Symbols
  SymbolList <- getSymbols(adjustedSymbols, src ="yahoo", from = start, to = end)
  
  #Set output window to record so that user can navigate charts using pg up/pg down
  windows(width =90, height = 60, record=TRUE)
  
  #Loop through adjusted symbol list and plot data
  counter<-1
  for(i in adjustedSymbols){
    
    #Make sure first ticker is loaded before waiting for next one
    if(counter>1){
      op <- par(ask=TRUE)
      on.exit(par(op))
    }
    
    #Parse i from loop to get appropriate variables
    x <- paste(deparse(as.name(i)), ".Close", sep="")
    y <- deparse(as.name(i))
    z <- eval(as.name(y))

    #Load closing data
    param <- z[, x]
    
     if(movingAverage == 0){
       chartSeries(z, dn.col = "red", name = y, TA="addVo()")
     }
     else{
       tmp <- paste(movingAverage, ")", sep="")
       taParam <- paste("addVo();addEMA(", tmp, sep = "" )
       chartSeries(z, dn.col = "red", name = y, TA=taParam)
    
     }
   
     #candleChart(z, up.col= "green", dn.col = "red", theme = "black",  name = y )
    
    #Increment loop
    counter <- counter + 1

  }
  #End window recording
  windows.options(record=FALSE)
}






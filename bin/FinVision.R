singlePortfolio <- function(TickerList, Size, start, end){ 
  
  library(rJava)
  library(quantmod)
  library(magrittr)
  .jinit()
  .jengine(TRUE)

  TickerList <- as.vector(TickerList)
  Size <- as.integer(Size)
  start <- as.Date(start)
  end <- as.Date(end)

  symbolFunction <- function(sym){
    return (tryCatch(getSymbols(sym, src = "yahoo", from = start, to = end), error= function(e) NULL))
  }
  adjustedSymbols <- unlist(lapply(TickerList, symbolFunction))
  SymbolList <- getSymbols(adjustedSymbols, src ="yahoo", from = start, to =end)
  
  windows(width =90, height = 60, record=TRUE)
  counter<-1
  for(i in adjustedSymbols){
    if(counter>1){
      op <- par(ask=TRUE)
      on.exit(par(op))
    }
    x <- paste(deparse(as.name(i)), ".Close", sep="")
    y <- deparse(as.name(i))
    z <- eval(as.name(y))
    if(is.null(z)){
      next
    }
    param <- z[, x]
    candleChart(z, up.col= "green", dn.col = "red", theme = "black",  name = y )
    #print(plot(param, main= paste(deparse(as.name(i)), "", sep="") , ylab="Price", xlab="Time"))
    counter <- counter + 1

  }
  windows.options(record=FALSE)
  
  
 
  n <- length(gg_list)
  nCol <- floor(sqrt(n))
  dev.new()
  do.call(grid.arrange, c(gg_list, ncol=nCol))
  
  return(AAPL)
  
}






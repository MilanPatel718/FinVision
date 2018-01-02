singlePortfolio <- function(TickerList, Size){ 
  
  library(rJava)
  library(quantmod)
  library(magrittr)
  .jinit()
  .jengine(TRUE)
  start <- as.Date("2008-01-01")
  end <- as.Date("2017-01-07")
  
  TickerList <- as.vector(TickerList)
  Size <- as.integer(Size)
  rows <- 2
  if(Size%%2!=0){
    rows= (Size/2) + 1
  }
  else{
    rows=Size/2
  }

  par(mfrow=c(rows,2))
  par(mar=c(1,1,1,1))
  SymbolList <- getSymbols(TickerList, src = "yahoo", from = start, to = end)
  
  for(i in SymbolList){
    x <- paste(deparse(as.name(i)), ".Close", sep="")
    y <- deparse(as.name(i))
    z <- eval(as.name(y))
    param <- z[, x]
    print(plot(param, main= paste(deparse(as.name(i)), "", sep="") , ylab="Price", xlab="Time"))
    
  }
  
  
  return(AAPL)
}





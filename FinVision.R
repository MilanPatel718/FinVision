singlePortfolio <- function(TickerList, Size, start, end){ 
  
  library(rJava)
  library(quantmod)
  library(magrittr)
  .jinit()
  .jengine(TRUE)
 # start <- as.Date("2016-11-01")
 # end <- as.Date("2017-01-07")
  TickerList <- as.vector(TickerList)
  Size <- as.integer(Size)
   start <- start
   end <- end
  rows <- 2
  if(Size%%2!=0){
    rows= (Size/2) + 1
  }
  else{
    rows=Size/2
  }

  #par(mfrow=c(rows,2))
  #par(mar=c(1,1,1,1))
  SymbolList <- getSymbols(TickerList, src = "yahoo", from = as.Date(start), to = as.Date(end))
  
  windows(width =90, height = 60, record=TRUE)
  counter<-1
  for(i in SymbolList){
    if(counter>1){
      op <- par(ask=TRUE)
      on.exit(par(op))
    }
    x <- paste(deparse(as.name(i)), ".Close", sep="")
    y <- deparse(as.name(i))
    z <- eval(as.name(y))
    param <- z[, x]
    candleChart(z, up.col= "green", dn.col = "red", theme = "white", name = y )
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







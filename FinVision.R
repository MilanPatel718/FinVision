singlePortfolio <- function(TickerList, Size){ 
    if (!require("rJava")) {
    install.packages("rJava")
    library(rJava)
  }
  
  if (!require("quantmod")) {
    install.packages("quantmod")
    library(quantmod)
  }
  
    if (!require("magrittr")) {
    install.packages("magrittr")
    library(magrittr)
   }
  
  
  start <- as.Date("2008-01-01")
  end <- as.Date("2017-01-07")
  
  TickerList <- as.vector(TickerList)
  Size <- as.integer(Size)
  
  TickerList= c("AMZN", "AAPL")
  SymbolList <- getSymbols(TickerList, src = "yahoo", from = start, to = end)
  gg_list <- list()
  j <- 1
  while(j <= Size){
    i <- SymbolList[j]
    a <- paste(deparse(as.name(i)), ".Close", sep="")
    b <- deparse(as.name(i))
    c <- eval(as.name(b))
    param <- c[, a]
    
    ggparam <- data.frame(date = index(param), price = param[index(param)])
    dev.new()
    gg_list[[j]] <- print.ggplot(data = (m = ggparam), aes_q(x = m[,1], y = m[,2])) + geom_point() +  geom_smooth(method = "lm") +
                                  labs(title = paste(b, "", sep=""), x = "Date", y = "Price")
    
    j <- j + 1
    
  }
  
 
  n <- length(gg_list)
  nCol <- floor(sqrt(n))
  dev.new()
  do.call(grid.arrange, c(gg_list, ncol=nCol))
  
  return(AAPL)
  
}







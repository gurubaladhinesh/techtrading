INSERT INTO SYMBOL_TYPE(ID,DESCRIPTION) VALUES('COMMODITY','Commodity symbol');
INSERT INTO SYMBOL(ID,COMPANY,DESCRIPTION,SYMBOL_TYPE) VALUES('NG','Natural Gas','Natural Gas','COMMODITY');
INSERT INTO CONTRACT_TYPE(ID,DESCRIPTION) VALUES('FUTURES','Futures contract');
INSERT INTO CONTRACT(ID, DESCRIPTION, END_DATE, IS_ACTIVE, KITE_CHART_ID, START_DATE, CONTRACT_TYPE, SYMBOL) VALUES('NGDEC19','Natural Gas Futures contract - December 2019','2019-12-25',TRUE,'55518983','2019-11-26','FUTURES','NG');
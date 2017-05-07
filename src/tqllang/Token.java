package tqllang;

// this is for the scanner ---> parser communication
// the scanner tells the parser the type of token it has seen
public enum Token
{
    // keywords
    selectToken,
    fromToken,
    whereToken,
    asToken,
    numberToken,
    identToken,
    defineToken,
    sensorToken,
    observationToken,
    sensorToObsToken,

    // arithmetic operators
    timesToken,
    divToken,
    plusToken,
    minusToken,

    // logical operators
    eqlToken,
    neqToken,
    lssToken,
    geqToken,
    leqToken,
    gtrToken,

    commaToken,
    openparenToken,
    closeparenToken,

    openbracketToken,
    closebracketToken,

    endOfFileToken,
    errorToken,

    periodToken,
    varToken,
    arrayToken,
    becomesToken,
    thenToken,
    doToken,
    semiToken,
    endToken,
    odToken,
    fiToken,
    elseToken,
    letToken,
    callToken,
    ifToken,
    whileToken,
    returnToken,
    funcToken,
    procToken,
    beginToken,
    mainToken
}

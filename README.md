# LazyJSONBenchmark

A hacky benchmark test for LazyJSON. All times referenced are in miliseconds. All tests were executed on a late 2013 13" macbook pro with a 2.4ghz core i5 cpu. If you have comments or suggestions for improvements to the benchmark, please reach out to me or file an issue here on github!

## SmallObject Tests

These tests all use an array containing 1000 copies of a small json object as their input. The object was specifically made to include each of JSON's scalar types as values. Each object has randomized numeric values, but generally looks as follows:

```javascript
{
	"key1":"value1",
	"key2":329898,
	"key3":0.00231,
	"key4":false,
	"key5":null
}
````

The source string for the SmallObject tests take up 83,182 characters.

### SmallObject Parse

This test simply makes each JSON parser parse the source string. In the case of the Jackson JsonParser it requests all tokens from the parser.

JSON Library | Min | Max | Avg | Median
-------------|-----|-----|-----|-------
json.org | 2.734555 | 9.187619 | 4.9613165 | 4.297115
GSON JsonParser | 1.16116 | 5.866435 | 1.977139 | 1.873002
Jackson ObjectMapper | 1.374046 | 4.939212 | 2.285925 | 2.145588
Jackson JsonParser | 0.46584 | 1.480218 | 0.803076625 | 0.77954
LazyJSON | 0.344015 | 1.261705 | 0.747316875 | 0.72561
GSON class based | 1.729185 | 4.541294 | 2.81834625 | 2.711455
Boon | 1.314129 | 4.477466 | 2.4179545 | 2.221195
LoganSquare | 1.020606 | 2.761957 | 1.812268375 | 1.800063

At this point, LazyJSON actually has a full AST structure build - much in the same way as Jackson's ObjectMapper API - yet, it is slightly faster than the fastest other parser in the test set: The Jacokson JsonParser, which does not build an AST.

### SmallObject Parse, Split and Serialize

This test represents the actual initial use case for LazyJSON. The ability to take a JSON array represented as a string, parse it, split it into objects and serializes each of these objects back out to a string.

JSON Library | Min | Max | Avg | Median
-------------|-----|-----|-----|-------
json.org | 4.097546 | 9.678625 | 6.3547365 | 6.17499
GSON JsonParser based | 1.873223 | 4.93902 | 3.17259775 | 3.090608
Jackson ObjectMapper | 2.104498 | 5.246862 | 3.4124225 | 3.445515
LazyJSON | 0.39939 | 1.470617 | 0.8138369375 | 0.784255
GSON class based | 2.896755 | 6.745232 | 4.661751 | 4.645749
Boon | 4.140396 | 9.548963 | 6.3975185 | 6.1944
LoganSquare | 1.766217 | 4.984128 | 3.0102225 | 3.036142

The nearest competitor is almost 4 times slower than LazyJSON. This is what it was designed for - any other use case is simply a bonus!

### SmallObject Parse and Access

When I initially wrote LazyJSON it seemed possible that it would be able to compete, performance wise, as a general JSON parser if you were only interested in one or two fields from the objects you were parsing. My theory was that the initial speed gain achieved by the lazy parsing strategy would quickly be lost to the faster class based parsers as you pulled out more and more fields.

However, that is not what I have found. The following test shows the results of pulling out all fields from the resulting objects. Thus forcing LazyJSON to do all the work it had initially skipped.

JSON Library | Min | Max | Avg | Median
-------------|-----|-----|-----|-------
json.org | 2.780589 | 7.107546 | 4.5699525 | 4.478677
GSON JsonParser | 1.749831 | 4.95721 | 3.21156575 | 3.101995
Jackson ObjectMapper | 1.25384 | 3.788166 | 2.499205 | 2.331968
Jackson JsonParser | 1.059711 | 3.117433 | 1.766779 | 1.752988
LazyJSON | 0.881185 | 3.473781 | 1.86775675 | 1.702428
GSON class based | 1.771563 | 4.403068 | 2.82594425 | 2.844848
Boon | 1.301495 | 3.130883 | 2.091181875 | 2.047607
LoganSquare | 0.981453 | 4.024667 | 2.034693125 | 1.986652

It is meaningfully faster than the fastest class based parser I have found (LoganSquare) and even faster than writing a hand written extraction of values using the token stream received from Jackson's JsonParser! I am sure there will be use cases where LazyJSON won't win out, but the initial expected limitation of it's design turned out to not be a real limitation at all.

## MediumObject Tests

These tests all use an array containing 1000 copies of a medium suzed json object as their input. This object was created to mimic some of the simple data envelopes in use at DoubleDutch. It uses the SimpleObject from the last test as its payload. While still generated with some aspect of randomness, it generaly looks as follows:

```javascript
{
	"id":"deadbeef-dead-beef-dead-beef00000001",
	"type":"MediumObject",
	"serial":234,
	"created":"Thu Jan 10 11:09:42 PT 1999",
	"data":[{
		"key1":"value1",
		"key2":329898,
		"key3":0.00231,
		"key4":false,
		"key5":null
	},{
		"key1":"value1",
		"key2":8932,
		"key3":0.75501,
		"key4":false,
		"key5":null
	}]
}
````

The source string for the MediumObject tests take up 297,193 characters.

### MediumObject Parse

JSON Library | Min | Max | Avg | Median
-------------|-----|-----|-----|-------
json.org | 5.605524 | 16.128994 | 8.785413 | 8.150946
GSON JsonParser | 2.910802 | 7.934705 | 4.905565 | 4.831509
Jackson ObjectMapper | 3.295039 | 12.96148 | 4.8930425 | 4.809485
Jackson JsonParser | 1.39476 | 4.020437 | 2.27061925 | 2.208585
LazyJSON | 1.068119 | 3.261092 | 1.765991 | 1.730452
GSON class based | 4.301452 | 9.415693 | 5.924457 | 5.758062
Boon | 3.130705 | 7.343901 | 4.834363 | 4.856269
LoganSquare | 2.613197 | 7.311305 | 4.6290735 | 4.54955


Running MediumObject split and serialize test
 + json.org | 11.026429 | 23.688277 | 14.817645 | 14.202038
 + GSON JsonParser based | 5.3741 | 13.533694 | 8.3247245 | 7.912941
 + Jackson ObjectMapper | 4.826591 | 11.808949 | 7.1937715 | 7.05345
 + LazyJSON | 1.021659 | 3.536178 | 1.866120625 | 1.8628
 + GSON class based | 7.554717 | 20.371266 | 10.714443 | 10.258093
 + Boon | 9.209956 | 17.921605 | 11.759534 | 11.150213
 + LoganSquare | 4.333113 | 10.601473 | 6.776118 | 6.524095
Generating LargeObject batch data
Running LargeObject parse test
 + json.org | 7.75176 | 22.426924 | 11.096008 | 10.59668
 + GSON JsonParser | 3.703044 | 9.933935 | 5.780482 | 5.690398
 + Jackson ObjectMapper | 2.882437 | 8.55358 | 5.165173 | 4.887188
 + Jackson JsonParser | 1.982652 | 6.068085 | 3.27319225 | 3.2957
 + LazyJSON | 1.590227 | 3.763087 | 2.54875 | 2.566992
 + GSON class based | 4.14453 | 12.330691 | 6.4768755 | 6.16929
 + Boon | 4.100606 | 9.142838 | 5.808073 | 5.70455
Running LargeObject split and serialize test
 + json.org | 16.193432 | 34.084046 | 20.677518 | 19.336549
 + GSON JsonParser based | 7.254918 | 17.019365 | 10.777558 | 10.150436
 + Jackson ObjectMapper | 5.149789 | 13.676147 | 7.4314225 | 7.19378
 + LazyJSON | 1.545074 | 4.47108 | 2.73187325 | 2.778728
 + GSON class based | 8.921176 | 20.0559 | 12.446913 | 12.08505
 + Boon | 10.468966 | 25.870486 | 14.332774 | 13.447765
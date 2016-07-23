# LazyJSONBenchmark
A hacky benchmark test for LazyJSON

## Running SmallObject parse test

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

Running SmallObject split and serialize test
 + json.org min:4.097546 max:9.678625 avg:6.3547365 med:6.17499
 + GSON JsonParser based min:1.873223 max:4.93902 avg:3.17259775 med:3.090608
 + Jackson ObjectMapper min:2.104498 max:5.246862 avg:3.4124225 med:3.445515
 + LazyJSON min:0.39939 max:1.470617 avg:0.8138369375 med:0.784255
 + GSON class based min:2.896755 max:6.745232 avg:4.661751 med:4.645749
 + Boon min:4.140396 max:9.548963 avg:6.3975185 med:6.1944
 + LoganSquare min:1.766217 max:4.984128 avg:3.0102225 med:3.036142
Running SmallObject parse and access test
 + json.org min:2.780589 max:7.107546 avg:4.5699525 med:4.478677
 + GSON JsonParser min:1.749831 max:4.95721 avg:3.21156575 med:3.101995
 + Jackson ObjectMapper min:1.25384 max:3.788166 avg:2.499205 med:2.331968
 + Jackson JsonParser min:1.059711 max:3.117433 avg:1.766779 med:1.752988
 + LazyJSON min:0.881185 max:3.473781 avg:1.86775675 med:1.702428
 + GSON class based min:1.771563 max:4.403068 avg:2.82594425 med:2.844848
 + Boon min:1.301495 max:3.130883 avg:2.091181875 med:2.047607
 + LoganSquare min:0.981453 max:4.024667 avg:2.034693125 med:1.986652
Generating MediumObject batch data
Running MediumObject parse test
 + json.org min:5.605524 max:16.128994 avg:8.785413 med:8.150946
 + GSON JsonParser min:2.910802 max:7.934705 avg:4.905565 med:4.831509
 + Jackson ObjectMapper min:3.295039 max:12.96148 avg:4.8930425 med:4.809485
 + Jackson JsonParser min:1.39476 max:4.020437 avg:2.27061925 med:2.208585
 + LazyJSON min:1.068119 max:3.261092 avg:1.765991 med:1.730452
 + GSON class based min:4.301452 max:9.415693 avg:5.924457 med:5.758062
 + Boon min:3.130705 max:7.343901 avg:4.834363 med:4.856269
 + LoganSquare min:2.613197 max:7.311305 avg:4.6290735 med:4.54955
Running MediumObject split and serialize test
 + json.org min:11.026429 max:23.688277 avg:14.817645 med:14.202038
 + GSON JsonParser based min:5.3741 max:13.533694 avg:8.3247245 med:7.912941
 + Jackson ObjectMapper min:4.826591 max:11.808949 avg:7.1937715 med:7.05345
 + LazyJSON min:1.021659 max:3.536178 avg:1.866120625 med:1.8628
 + GSON class based min:7.554717 max:20.371266 avg:10.714443 med:10.258093
 + Boon min:9.209956 max:17.921605 avg:11.759534 med:11.150213
 + LoganSquare min:4.333113 max:10.601473 avg:6.776118 med:6.524095
Generating LargeObject batch data
Running LargeObject parse test
 + json.org min:7.75176 max:22.426924 avg:11.096008 med:10.59668
 + GSON JsonParser min:3.703044 max:9.933935 avg:5.780482 med:5.690398
 + Jackson ObjectMapper min:2.882437 max:8.55358 avg:5.165173 med:4.887188
 + Jackson JsonParser min:1.982652 max:6.068085 avg:3.27319225 med:3.2957
 + LazyJSON min:1.590227 max:3.763087 avg:2.54875 med:2.566992
 + GSON class based min:4.14453 max:12.330691 avg:6.4768755 med:6.16929
 + Boon min:4.100606 max:9.142838 avg:5.808073 med:5.70455
Running LargeObject split and serialize test
 + json.org min:16.193432 max:34.084046 avg:20.677518 med:19.336549
 + GSON JsonParser based min:7.254918 max:17.019365 avg:10.777558 med:10.150436
 + Jackson ObjectMapper min:5.149789 max:13.676147 avg:7.4314225 med:7.19378
 + LazyJSON min:1.545074 max:4.47108 avg:2.73187325 med:2.778728
 + GSON class based min:8.921176 max:20.0559 avg:12.446913 med:12.08505
 + Boon min:10.468966 max:25.870486 avg:14.332774 med:13.447765
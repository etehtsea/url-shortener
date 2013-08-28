# url-shortener

Demo application based on example from perfect [Clojure
programming book][cpb]:


## Installation

Download from http://example.com/FIXME.

## Usage

Run locally:

```sh
lein run [port]
```

or

```sh
$ lein uberjar
$ java -jar url-shortener-standalone.jar [port]
```

## Deployment

```sh
$ heroku apps:create
$ git push heroku master
$ heroku open
```

See [heroku documentation][hcljdocs] for further instructions.

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[cpb]: http://shop.oreilly.com/product/0636920013754.do
[hcljdocs]: https://devcenter.heroku.com/articles/clojure

# lein-coffee

A Leiningen plugin thinly wrapping the CoffeeScript compiler.

The current implementation depends on lein-npm and lein-shell. It fetches a suitable coffee-script node package via lein-npm, and runs its coffee executable via lein-shell.

## Usage

Put `[lein-coffee "0.1.0"]` into the `:plugins` vector of your project.clj.

### Configuration

lein-coffee accepts these parameters in your project.clj:

```clj
:coffee-version ">=1.6.3"
:coffee-sources ["src/main/coffee/foo.coffee"]
:coffee-join "foo.js"
:coffee-output "target/resources/lib/foo/js"
:coffee-bare true
:coffee-watch false
```

These correspond to a subset of the usual flags provided by the coffee executable.

### Example

To automatically run the compiler with the configured project settings

    $ lein coffee

To run the compiler in watch mode

    $ lein coffee watch

To run the coffee executable with arbitrary flags and arguments at the command line

    $ lein coffee run --version

The last form can be useful in your project.clj with :prep-tasks or :aliases.

## License

Copyright © 2014 SMX

Distributed under the Eclipse Public License.

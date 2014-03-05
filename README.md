# lein-coffee

A Leiningen plugin thinly wrapping the CoffeeScript compiler.

The current implementation depends on lein-npm and lein-shell. It fetches a suitable coffee-script node package via lein-npm, and runs its coffee executable via lein-shell.

## Usage

Put `[lein-coffee "0.1.3"]` into the `:plugins` vector of your project.clj.

lein-coffee adds hooks to the `compile` and `jar` tasks, so all you need to do is configure it in your project.clj.

### Configuration

lein-coffee accepts these parameters in your project.clj:

```clj
:lein-coffee
{:compile-hook true ;; Invoke coffee at `lein compile`
 :jar-hook true ;; Invoke coffee at `lein jar`
 :coffee
 {:version ">=1.6"
  :sources ["src/main/coffee/foo.coffee"]
  :join "foo.js"
  :output "target/resources/lib/foo/js"
  :bare true
  :watch false ;; Run coffee with `-w` (you probably don't want this enabled here)
  }}
```

These correspond to a subset of the usual flags provided by the coffee executable.

### Example

To run the compiler with the configured project settings

    $ lein compile

Or, if you want to invoke coffee explicitly, which does the same thing

    $ lein coffee

To run the just the coffee compiler in watch mode (this is preferred instead of `:watch true` in `project.clj`)

    $ lein coffee watch

To run the coffee executable with arbitrary flags and arguments at the command line

    $ lein coffee run --version

The last form can be useful in your project.clj with `:prep-tasks` or `:aliases` if you need a sophisticated invocation.

## License

Copyright © 2014 SMX

Distributed under the Eclipse Public License.

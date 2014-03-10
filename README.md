# lein-coffee

A Leiningen plugin thinly wrapping the CoffeeScript compiler.

The current implementation depends on lein-npm and lein-shell. It fetches a suitable coffee-script node package via lein-npm, and runs its coffee executable via lein-shell.

## Usage

Put `[lein-coffee "0.2.1"]` into the `:plugins` vector of your project.clj.

lein-coffee adds hooks to the `compile` and `jar` tasks, so all you need to do is configure it in your project.clj.

### Configuration

lein-coffee accepts these parameters in your project.clj, shown here for a single invocation of the coffee executable:

```clj
:lein-coffee
{:compile-hook true ;; Invoke coffee at `lein compile`
 :jar-hook true ;; Invoke coffee at `lein jar`
 :coffee-version ">=1.6"
 :coffee
 {:sources ["src/main/coffee/foo.coffee"]
  :join "foo.js"
  :output "target/resources/lib/foo/js"
  :bare true
  :watch false ;; Run coffee with `-w` (you probably don't want this enabled here)
  }}
```

These correspond to a subset of the usual flags provided by the coffee executable.

You can also tell lein-coffee to run multiple passes of the coffee executable, by passing in a vector of `:invocations`:

:lein-coffee
{:compile-hook true ;; Invoke coffee at `lein compile`
 :jar-hook true ;; Invoke coffee at `lein jar`
 :coffee-version ">=1.6"
 :coffee
 {:bare true
  :watch false ;; Run coffee with `-w` (you definitely don't want this enabled here, or it will block)
  :invocations
  [{:sources ["src/main/coffee/foo.coffee"]
    :join "foo.js"
    :output "target/resources/lib/foo/js"
  },
  {:sources ["src/main/coffee/foo/bar.coffee", "src/main/coffee/foo/baz.coffee"]
   :join "bar-baz.js"
   :output "target/resources/lib/foo/js"
  }]}}
```

Each invocation map inherits relevant default values from the `:coffee` config.

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

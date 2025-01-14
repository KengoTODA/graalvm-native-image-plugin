#!/usr/bin/env -S jq -f

# expect github repos release API as input

[
  .[] |
  .name |
  select(contains("RC") == false) |
  select(contains("M1") == false) |
  {version: ., indices: indices(".")} |
  {version: .version, index: (.indices[1] // (.version | utf8bytelength))} |
  {version: .version, majorMinor: .version[:.index]}
] |
group_by(.majorMinor) |
reverse |
[ .[] | max_by(.version) | .version ] |
.[:7] as $versions |

{} |

.name = "version availability test" |
.on = {
  push: {
    branches: ["main"]
  },
  workflow_dispatch: {
    inputs: {
      memo: {
        description: "memo",
        required: false
      }
    }
  }
} |

def strategy(vs):
  {} |
  .matrix = {} |
  .matrix.version = vs
;

def runs(name; run; environment; id):
  {} |
  if name != null then .name = name else . end |
  .run = run |
  if id != null then .id = id else . end |
  if (environment != null) and (environment | type == "object")
    then .env = environment
    else .
  end
;

def uses(name; action; with; id):
  {} |
  if name != null then .name = name else . end |
  if id != null then .id = id else . end |
  if action | type == "string"
    then .uses = action
    else .uses = "\(action.owner)/\(action.action)@\(action.version)"
  end |
  if with != null then .with = with else . end
;

.jobs.test.name = "Gradle-${{ matrix.version }} nativeImage" |
.jobs.test["runs-on"] = "ubuntu-20.04" |
.jobs.test.strategy.matrix.version = $versions |
.jobs.test.steps = [
  uses("Checkout"; "actions/checkout@v2"; null; null),
  uses(
    "Setup GraalVM";
    "DeLaGuardo/setup-graalvm@8bbfe44ef9c6f5c07e5af036a1bffd561c037d18";
    {"graalvm-version": "20.3.0.java11"};
    null),
  runs("Install GraalVM native image"; "gu install native-image"; null; null),
  uses(
    "Cache Gradle Wrapper";
    { owner: "actions", action: "cache", version: "v1" };
    {
      path: "~/.gradle/caches\n~/.gradle/wrapper\n.cache",
      key: "wrapper-${{ matrix.version }}",
      "restore-keys": "wrapper-"
    };
    "gradle"),
  runs(
    "Run Test";
    "./version-tests/test.sh";
    { GRADLE_VERSION: "${{ matrix.version }}", TOKEN: "${{ secrets.GITHUB_TOKEN }}" };
    null)
]

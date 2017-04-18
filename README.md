# Codeshooter

Run:

```
$ gradle build
$ java -jar build/libs/codeshooter-0.0.1-SNAPSHOT.jar
```

Play around with your shooter (arrow keys to move, spacebar to shoot),
and meanwhile launch the following in separate terminals:

```
$ java -cp build/libs/codeshooter-0.0.1-SNAPSHOT.jar codeshooter.ai.RemoteHunterShooterBot localhost 5781
```

and

```
$ java -cp build/libs/codeshooter-0.0.1-SNAPSHOT.jar codeshooter.ai.RemoteHunterShooterBot localhost 5782
```

There's also `codeshooter.ai.RemoteRandomShooterBot`, which is less
interesting.

# youtube-watch

This example shows how to use [Selenium](https://www.selenium.dev/) to start [YouTube](https://www.youtube.com/) videos and restart them later at the same time the web page was left.

It demonstrates:
- how to navigate to a web page
- how to store and reuse cookies
- how to execute JavaScript in the context of the loaded web page

## Build and Run

Make sure that [geckodriver](https://github.com/mozilla/geckodriver/releases) exists in path.

    gradle build
    gradle distTar
    tar -xf build/distributions/youtube-watch.tar -C /tmp
    /tmp/youtube-watch/bin/youtube-watch <video-id>

## License

Note that the source code itself is licensed under the terms of the Unlicense.
But the project makes use of dependencies that are licensed under different terms.

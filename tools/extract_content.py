"""Extract readable content outline from a static HTML page.

Reads an untrusted HTML file (passed as argv[1]) as data only. No execution.
Usage: python -I tools/extract_content.py <file.html>
"""
import re
import sys
from html.parser import HTMLParser

BLOCK = {"h1", "h2", "h3", "h4", "h5", "p", "li", "dt", "dd", "blockquote", "pre"}
SKIP = {"style", "script", "svg", "head", "title", "noscript"}


class Outline(HTMLParser):
    def __init__(self):
        super().__init__(convert_charrefs=True)
        self.skip_depth = 0
        self.stack = []
        self.buf = []
        self.current = None
        self.anchor = None
        self.out = []

    def handle_starttag(self, tag, attrs):
        a = dict(attrs)
        if tag in SKIP:
            self.skip_depth += 1
            return
        if self.skip_depth:
            return
        if tag in ("section", "header", "footer", "main", "nav") and a.get("id"):
            self.anchor = a["id"]
        if tag in BLOCK:
            self.current = tag
            self.buf = []

    def handle_endtag(self, tag):
        if tag in SKIP:
            self.skip_depth = max(0, self.skip_depth - 1)
            return
        if self.skip_depth:
            return
        if tag in BLOCK and self.current == tag:
            text = re.sub(r"\s+", " ", "".join(self.buf)).strip()
            if text:
                prefix = f"[{self.anchor}] " if self.anchor else ""
                self.out.append(f"{prefix}<{tag}> {text}")
                self.anchor = None
            self.current = None
            self.buf = []

    def handle_data(self, data):
        if self.skip_depth:
            return
        if self.current:
            self.buf.append(data)


def main():
    with open(sys.argv[1], "r", encoding="utf-8", errors="replace") as fh:
        html = fh.read()
    p = Outline()
    p.feed(html)
    print("\n".join(p.out))


if __name__ == "__main__":
    main()

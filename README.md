
## SASS

```bash
$ sudo gem install sass
Password:
Fetching: sass-3.3.11.gem (100%)
Successfully installed sass-3.3.11
1 gem installed

$ sass -v
Sass 3.3.11 (Maptastic Maple)

$ sass-convert --help
```

Convert `css` to `scss` (Do this only the first time)

```bash
$ sass-convert -T scss stylesheets/styles.css stylesheets/styles.scss 
```

Generate CSS

```bash
$ sass stylesheets/styles.scss > stylesheets/styles.css
```

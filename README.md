# Sockets
This is a program to have multiple users connect and chat.
A problem we experienced was with StringUtils:
  One way we fixed this was to dowload a module to be able to import StringUtils.
  The other way was to instead of putting String[] tokens = StringUtils.split(line); You put String[] tokens = Line.split("");

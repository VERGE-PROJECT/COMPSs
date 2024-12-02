#!/usr/bin/python
#
#  Copyright 2002-2023 Barcelona Supercomputing Center (www.bsc.es)
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

# Data types source file:
JAVA_PATH = "../java/api/src/main/java/es/bsc/compss/types/annotations/parameter/DataType.java"
# Data types destination files:
CPP_PATH = "./bindings-common/src/data_type.h"
PYTHON_PATH = "./python/src/pycompss/api/commons/data_type.py"

# Destination files header comments:
NOTIFICATION = [
    "Autogenerated file (see generate_datatype_enums.py).",
    " - Uses DataType.java as the source template.",
]

# Enable/disable debugging
DEBUG = True


def get_datatypes():
    """Get the defined types (and in the right order).

    :return: List of string with the types in order.
    """
    if DEBUG:
        print("- Getting datatypes...")
    datatypes = []
    already = False
    for line in open(JAVA_PATH, "r"):
        if "public enum" in line:
            already = True
            continue
        if already:
            already = not line.startswith("}")
        if already:
            cur_type = line.split()[0].replace(",", "").replace(";", "").strip()
            datatypes.append(cur_type)
            already = not line.startswith("}")
    if DEBUG:
        i = 0
        for dt in datatypes:
            print("  - %s - %s" % (str(i), str(dt)))
            i += 1
    return datatypes


def create_c_types(datatypes):
    """Create the h file to match types from the C/C++ binding.

    :param datatypes: List of data types and integer that represents it.
    :return: None.
    """
    if DEBUG:
        print("- Creating C/C++ datatypes header...")
    with open(CPP_PATH, "w") as f:
        f.write("\n")
        for notification_line in NOTIFICATION:
            f.write("// %s\n" % notification_line)
        f.write("enum datatype {\n    ")

        first = True
        for line in datatypes:
            line = line.lower().replace("_t", "_dt")
            if first:
                line += " = 0"
            else:
                line = ",\n    " + line
            f.write(line)
            first = False

        f.write("\n};\n")
    if DEBUG:
        print("- Created C/C++ datatypes header")


def create_python_types(datatypes):
    """Create the Python file to match types from the Python binding.

    :param datatypes: List of data types and integer that represents it.
    :return: None.
    """
    if DEBUG:
        print("- Creating Python datatypes file...")
    module_docstring = ['"""',
                        "Python binding to runtime data types matching.",
                        ""
    ]
    module_docstring = module_docstring + NOTIFICATION
    module_docstring.append('"""')

    with open(PYTHON_PATH, "w") as f:
        for module_docstring_line in module_docstring:
            f.write("%s\n" % module_docstring_line)
        f.write("\n\n")
        f.write("class SupportedDataTypes:  # pylint: disable=too-few-public-methods\n")
        f.write('    """Python binding to runtime Datatypes."""\n\n')
        for (i, line) in enumerate(datatypes):
            f.write("    %s = %d\n" % (line.replace("_T", ""), i))
        f.write("\n\n")
        f.write("DataType = SupportedDataTypes()")
        f.write("\n")
    if DEBUG:
        print("- Created Python datatypes file")


def main():
    """Get the defined types (in order) and create C/C++ and python types.

    :return: None.
    """
    line = "------------------------------------------------"
    if DEBUG:
        print(line)
        print("Generating datatype enum types...")

    # Get the defined types (and in the right order)
    datatypes = get_datatypes()

    # Create C/C++ datatypes header
    create_c_types(datatypes)

    # Create Python datatypes file
    create_python_types(datatypes)

    if DEBUG:
        print("Datatype enum types generated successfully")
        print(line)


if __name__ == "__main__":
    main()

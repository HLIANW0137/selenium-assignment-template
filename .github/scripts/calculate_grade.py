#!/usr/bin/env python3
"""
Selenium Assignment - Points Calculator
Parses points.yml and calculates the expected grade.
"""

import sys
import yaml

# -----------------------------------------------------------
# Point definitions
# -----------------------------------------------------------
BASIC_TASKS = [
    ("login_form",          3,  "once",       None),
    ("form_with_user",      3,  "once",       None),
    ("logout",              2,  "once",       None),
    ("fill_input",          1,  "repeatable", 10),
    ("send_form",           1,  "repeatable", 10),
    ("static_page_test",    2,  "once",       None),
    ("multiple_page_test",  3,  "once",       None),
    ("complex_xpath",       1,  "repeatable", 10),
    ("textarea",            1,  "once",       None),
    ("dropdown",            2,  "once",       None),
    ("radio_button",        1,  "once",       None),
    ("at_least_4_classes",  3,  "once",       None),
    ("at_least_6_classes",  3,  "once",       None),
    ("at_least_8_classes",  3,  "once",       None),
    ("explicit_wait",       3,  "once",       None),
    ("page_title",          1,  "once",       None),
    ("page_object_pattern", 3,  "once",       None),
    ("base_page_class",     1,  "once",       None),
    ("readable_tests",      3,  "once",       None),
]

ADVANCED_TASKS = [
    ("webdriver_config",      4,  "once", None),
    ("cookie_manipulation",   6,  "once", None),
    ("hover_test",            6,  "once", None),
    ("drag_and_drop",         8,  "once", None),
    ("file_upload",           6,  "once", None),
    ("history_test",          4,  "once", None),
    ("test_dependencies",     4,  "once", None),
    ("email_checking",        16, "once", None),
    ("random_data",           8,  "once", None),
    ("download_files",        12, "once", None),
    ("config_file",           6,  "once", None),
    ("javascript_executor",   4,  "once", None),
    ("screenshot_on_failure", 6,  "once", None),
    ("headless_execution",    4,  "once", None),
    ("cross_browser_testing", 8,  "once", None),
    ("dockerized_execution",  4,  "once", None),
]

# Grade thresholds: (grade, min_overall, min_advanced)
GRADE_THRESHOLDS = [
    (5, 80, 20),
    (4, 60, 10),
    (3, 40, 5),
    (2, 25, 0),
]

# Quality gatekeepers: (key, description, max_grade_without)
# If not done, your grade is capped at max_grade_without
QUALITY_GATES = [
    ("gitignore",        "Unnecessary files are ignored (.gitignore)",          1),
    ("structured_code",  "Code is organized into classes and functions",        2),
    ("readable_code",    "Readable code and descriptive method names",          3),
    ("low_redundancy",   "Low code redundancy, shared logic extracted",         4),
]


def load_points(path):
    with open(path, "r") as f:
        return yaml.safe_load(f)


def validate_student(data):
    student = data.get("student", {})
    errors = []
    if not student.get("website_under_test"):
        errors.append("Website under test is empty")
    return student, errors


def calc_task_points(section_data, task_defs):
    total = 0
    lines = []

    for key, pts_per, task_type, max_count in task_defs:
        task = section_data.get(key, {})
        desc = task.get("description", key)
        earned = 0

        if task_type == "once":
            if task.get("done", False):
                earned = pts_per
                lines.append(f"  [x] {desc}: {earned} pts")
            else:
                lines.append(f"  [ ] {desc}: 0 pts")
        elif task_type == "repeatable":
            count = task.get("count", 0)
            if not isinstance(count, int) or count < 0:
                count = 0
            capped = min(count, max_count) if max_count else count
            earned = capped * pts_per
            if count > 0:
                if count > capped:
                    lines.append(f"  [x] {desc}: {capped}x = {earned} pts (capped from {count})")
                else:
                    lines.append(f"  [x] {desc}: {count}x = {earned} pts")
            else:
                lines.append(f"  [ ] {desc}: 0 pts")

        total += earned

    return total, lines


def check_quality(data):
    quality_data = data.get("quality", {})
    max_grade = 5
    lines = []

    for key, desc, cap_at in QUALITY_GATES:
        task = quality_data.get(key, {})
        done = task.get("done", False)
        if done:
            lines.append(f"  [x] {desc}")
        else:
            lines.append(f"  [ ] {desc} --> grade capped at {cap_at}")
            max_grade = min(max_grade, cap_at)

    return max_grade, lines


def determine_grade(overall, advanced):
    for grade, min_overall, min_advanced in GRADE_THRESHOLDS:
        if overall >= min_overall and advanced >= min_advanced:
            return grade
    return 1


def main():
    path = sys.argv[1] if len(sys.argv) > 1 else "points.yml"

    try:
        data = load_points(path)
    except FileNotFoundError:
        print("ERROR: points.yml not found")
        sys.exit(1)
    except yaml.YAMLError as e:
        print(f"ERROR: Invalid YAML syntax in points.yml\n{e}")
        print()
        print("Tip: check your YAML at https://www.yamllint.com/")
        sys.exit(1)

    # Student info
    student, errors = validate_student(data)
    print("=" * 60)
    print("SELENIUM ASSIGNMENT - GRADE REPORT")
    print("=" * 60)
    print()
    print(f"Website:  {student.get('website_under_test', '(empty)')}")
    print()

    if errors:
        for err in errors:
            print(f"WARNING: {err}")
        print()

    # Quality gatekeepers
    quality_cap, quality_lines = check_quality(data)

    print("-" * 60)
    print("QUALITY REQUIREMENTS")
    print("-" * 60)
    for line in quality_lines:
        print(line)
    print()

    # Basic tasks
    basic_data = data.get("basic", {})
    basic_total, basic_lines = calc_task_points(basic_data, BASIC_TASKS)

    print("-" * 60)
    print("BASIC TASKS")
    print("-" * 60)
    for line in basic_lines:
        print(line)
    print()
    print(f"  Basic total: {basic_total} pts")
    print()

    # Advanced tasks
    advanced_data = data.get("advanced", {})
    advanced_total, advanced_lines = calc_task_points(advanced_data, ADVANCED_TASKS)

    print("-" * 60)
    print("ADVANCED TASKS")
    print("-" * 60)
    for line in advanced_lines:
        print(line)
    print()
    print(f"  Advanced total: {advanced_total} pts")
    print()

    # Summary
    overall = basic_total + advanced_total
    points_grade = determine_grade(overall, advanced_total)
    final_grade = min(points_grade, quality_cap)

    print("=" * 60)
    print("  Grade thresholds:")
    print("    Grade 2: 25 overall,  0 advanced")
    print("    Grade 3: 40 overall,  5 advanced")
    print("    Grade 4: 60 overall, 10 advanced")
    print("    Grade 5: 80 overall, 20 advanced")
    print()
    print("=" * 60)
    print("SUMMARY")
    print("=" * 60)
    print()
    print(f"  Overall points:    {overall}")
    print(f"  Advanced points:   {advanced_total}")
    print(f"  Grade from points: {points_grade}")
    print(f"  Quality cap:       {quality_cap}")
    
    if final_grade < points_grade:
        print(f"  Your points would give you grade {points_grade},")
        print(f"  but quality requirements cap it at {final_grade}.")
        print()

    print("=" * 60)
    if errors:
        print("  NOT GRADEABLE — fill in your website_under_test first!")
    else:
        print(f"  Final grade:       {final_grade}")
    print("=" * 60)
    print()


if __name__ == "__main__":
    main()

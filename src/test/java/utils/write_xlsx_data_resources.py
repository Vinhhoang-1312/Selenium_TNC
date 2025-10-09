# Script to create testdata.xlsx in src/test/resources with correct login test data
import openpyxl
from openpyxl import Workbook
import os

os.makedirs(r"C:\Users\tranp\IdeaProjects\TNC_S\src\test\resources", exist_ok=True)
wb = Workbook()
ws = wb.active
ws.title = "LoginData"

# Header
ws.append(["TestId", "Email", "Password"])
# Data rows
ws.append(["valid", "john@test.com", "Abc12345"])
ws.append(["invalidEmail1", "test@", "Abc12345"])
ws.append(["emptyEmail", "", "Abc12345"])
ws.append(["emptyPassword", "john@test.com", ""])
ws.append(["bothEmpty", "", ""])

wb.save(r"C:\Users\tranp\IdeaProjects\TNC_S\src\test\resources\testdata.xlsx")

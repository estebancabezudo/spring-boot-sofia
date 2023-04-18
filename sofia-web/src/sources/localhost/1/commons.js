Core.setReportSystemErrorFunction(error => {
    Core.showErrorMessage(error.message);
    throw error;
});
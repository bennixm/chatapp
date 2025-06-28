
import Swal from 'sweetalert2';

const baseConfig = {
    customClass: { popup: "custom-popup" },
    background: "#000000e0",
    color: "#ffffff",
    confirmButtonColor: "#009ca6",
    heightAuto: false,
};

export function showSuccessAlert(title, text, timer = 2000) {
    return Swal.fire({
        ...baseConfig,
        icon: 'success',
        title,
        text,
        timer,
        showConfirmButton: false,
    });
}

export function showErrorAlert(title, text) {
    return Swal.fire({
        ...baseConfig,
        icon: 'error',
        title,
        text,
    });
}

export function showWarningAlert(title, text) {
    return Swal.fire({
        ...baseConfig,
        icon: 'warning',
        title,
        text,
    });
}

export function showInfoAlert(title, text) {
    return Swal.fire({
        ...baseConfig,
        icon: 'info',
        title,
        text,
    });
}

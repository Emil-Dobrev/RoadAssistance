export function copyToClipboard(text: string | undefined) {
    if (text) {
        navigator.clipboard.writeText(text).then(() => {
            // TODO add here toast message
            alert('Phone number copied!');
        });
    }
}
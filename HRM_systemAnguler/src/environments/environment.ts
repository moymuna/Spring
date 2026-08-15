// Single place to point the app at its backend.
const baseUrl = 'http://localhost:8085';
export const environment = {
  production: false,
  baseUrl,
  apiUrl: `${baseUrl}/api/`,
  imgUrl: `${baseUrl}/images/`,
};
export const SECRET = 'CM$3curEK3y!2026';

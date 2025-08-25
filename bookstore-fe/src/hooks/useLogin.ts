import { useState } from "react";

interface LoginResponse {
    token: string;
    user: {
        id: number;
        username: string;
        email: string;
    };
}

export function useLogin() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const login = async (username: string, password: string): Promise<LoginResponse | null> => {
        setLoading(true);
        setError(null);

        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            if (!res.ok) {
                throw new Error("Invalid username or password");
            }

            const data: LoginResponse = await res.json();
            return data;
        } catch (err: any) {
            setError(err.message || "Something went wrong");
            return null;
        } finally {
            setLoading(false);
        }
    };

    return { login, loading, error };
}
